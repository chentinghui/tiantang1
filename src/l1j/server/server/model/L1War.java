/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.WarTimeController;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_War;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.world.L1World;

// Referenced classes of package l1j.server.server.model:
// L1War

public class L1War {
	private String _param1 = null;
	private String _param2 = null;
	private final ArrayList<String> _attackClanList = new ArrayList<String>();
	private String _defenceClanName = null;
	private int _warType = 0;
	private int _castleId = 0;
	private L1Castle _castle = null;
	private Calendar _warEndTime;

	private boolean _isWarTimerDelete = false;

	public L1War() {
	}

	class CastleWarTimer implements Runnable {
		public CastleWarTimer() {
		}

		@Override
		public void run() {
			for (;;) {
				try {
					Thread.sleep(1000);
					if (_warEndTime.before(WarTimeController.getInstance()
							.getRealTime())) {
						break;
					}
				} catch (Exception exception) {
					break;
				}
				if (_isWarTimerDelete) { // 战争终结终了
					return;
				}
			}
			CeaseCastleWar(); // 攻城战终结处理
			delete();
		}
	}

	class SimWarTimer implements Runnable {
		public SimWarTimer() {
		}

		@Override
		public void run() {
			for (int loop = 0; loop < 240; loop++) { // 240分
				try {
					Thread.sleep(60000);
				} catch (Exception exception) {
					break;
				}
				if (_isWarTimerDelete) { // 战争终结终了
					return;
				}
			}
			CeaseWar(_param1, _param2); // 终结
			delete();
		}
	}

	public void handleCommands(int war_type, String attack_clan_name,
			String defence_clan_name) {
		// war_type - 1:攻城战 2:模拟战
		// attack_clan_name - 布告名
		// defence_clan_name - 布告名（攻城战时、城主）

		SetWarType(war_type);

		DeclareWar(attack_clan_name, defence_clan_name);

		_param1 = attack_clan_name;
		_param2 = defence_clan_name;
		InitAttackClan();

		AddAttackClan(attack_clan_name);
		SetDefenceClanName(defence_clan_name);

		if (war_type == 1) { // 攻城战
			_castleId = GetCastleId();
			_castle = GetCastle();
			if (_castle != null) {
				Calendar cal = (Calendar) _castle.getWarTime().clone();
				cal.add(Config.ALT_WAR_TIME_UNIT, Config.ALT_WAR_TIME);
				_warEndTime = cal;
			}

			CastleWarTimer castle_war_timer = new CastleWarTimer();
			GeneralThreadPool.getInstance().execute(castle_war_timer); // 开始
		} else if (war_type == 2) { // 模拟战
			SimWarTimer sim_war_timer = new SimWarTimer();
			GeneralThreadPool.getInstance().execute(sim_war_timer); // 开始
		}
		L1World.getInstance().addWar(this); // 战争追加
	}

	private void RequestCastleWar(int type, String clan1_name, String clan2_name) {
		if (clan1_name == null || clan2_name == null) {
			return;
		}

		L1Clan clan1 = L1World.getInstance().getClan(clan1_name);
		if (clan1 != null) {
			L1PcInstance clan1_member[] = clan1.getOnlineClanMember();
			for (int cnt = 0; cnt < clan1_member.length; cnt++) {
				clan1_member[cnt].sendPackets(new S_War(type, clan1_name,
						clan2_name));
			}
		}

		int attack_clan_num = GetAttackClanListSize();
		
		if (type == 1 || type == 2 || type == 3) { // 宣战布告、降伏、终结
			L1Clan clan2 = L1World.getInstance().getClan(clan2_name);
			if (clan2 != null) {
				int castle_id =  clan2.getCastleId();
				if (GetWarType() == 1) {
					L1PcInstance clan1_member[] = clan1.getOnlineClanMember();
					for (int cnt = 0; cnt < clan1_member.length; cnt++) {
						if (L1CastleLocation.checkInWarArea(castle_id, clan1_member[cnt])) {
							if (!clan1_member[cnt].isDead()){
								// 旗内居
								int[] loc = L1CastleLocation.getGetBackLoc(castle_id);
								int locx = loc[0];
								int locy = loc[1];
								short mapid = (short) loc[2];
								L1Teleport.teleport(clan1_member[cnt], locx, locy, mapid, 5, true);
							}
						}
					}			
				}

				L1PcInstance clan2_member[] = clan2.getOnlineClanMember();
				for (int cnt = 0; cnt < clan2_member.length; cnt++) {
					if (type == 1) { // 宣战布告
						clan2_member[cnt].sendPackets(new S_War(type,
								clan1_name, clan2_name));
					} else if (type == 2) { // 降伏
						clan2_member[cnt].sendPackets(new S_War(type,
								clan1_name, clan2_name));
						if (attack_clan_num == 1) { // 攻击侧一
							clan2_member[cnt].sendPackets(new S_War(4,
									clan2_name, clan1_name));
						} else {
							clan2_member[cnt].sendPackets(new S_ServerMessage( // %0血盟%1血盟降伏。
									228, clan1_name, clan2_name));
							RemoveAttackClan(clan1_name);
						}
					} else if (type == 3) { // 终结
						clan2_member[cnt].sendPackets(new S_War(type,
								clan1_name, clan2_name));
						if (attack_clan_num == 1) { // 攻击侧一
							clan2_member[cnt].sendPackets(new S_War(4,
									clan2_name, clan1_name));
						} else {
							clan2_member[cnt].sendPackets(new S_ServerMessage( // %0血盟%1血盟间战争终结。
									227, clan1_name, clan2_name));						
							RemoveAttackClan(clan1_name);					
						}
					}
				}
			}
		}

		if ((type == 2 || type == 3) && attack_clan_num >= 1) { // 降伏、终结攻击侧一
			_isWarTimerDelete = true;
			delete();
		}
	}

	private void RequestSimWar(int type, String clan1_name, String clan2_name) {
		if (clan1_name == null || clan2_name == null) {
			return;
		}

		L1Clan clan1 = L1World.getInstance().getClan(clan1_name);
		if (clan1 != null) {
			L1PcInstance clan1_member[] = clan1.getOnlineClanMember();
			for (int cnt = 0; cnt < clan1_member.length; cnt++) {
				clan1_member[cnt].sendPackets(new S_War(type, clan1_name,
						clan2_name));
			}
		}

		if (type == 1 || type == 2 || type == 3) { // 宣战布告、降伏、终结
			L1Clan clan2 = L1World.getInstance().getClan(clan2_name);
			if (clan2 != null) {
				L1PcInstance clan2_member[] = clan2.getOnlineClanMember();
				for (int cnt = 0; cnt < clan2_member.length; cnt++) {
					if (type == 1) { // 宣战布告
						clan2_member[cnt].sendPackets(new S_War(type,
								clan1_name, clan2_name));
					} else if (type == 2 || type == 3) { // 降伏、终结
						clan2_member[cnt].sendPackets(new S_War(type,
								clan1_name, clan2_name));
						clan2_member[cnt].sendPackets(new S_War(4, clan2_name,
								clan1_name));
					}
				}
			}
		}

		if (type == 2 || type == 3) { // 降伏、终结
			_isWarTimerDelete = true;
			delete();
		}
	}

	public void WinCastleWar(String clan_name) { // 夺取、攻击侧胜利
		String defence_clan_name = GetDefenceClanName();
		L1World.getInstance().broadcastPacketToAll(new S_ServerMessage( // %0血盟%1血盟战争胜利。
				231, clan_name, defence_clan_name));

		L1Clan defence_clan = L1World.getInstance().getClan(defence_clan_name);
		if (defence_clan != null) {
			L1PcInstance defence_clan_member[] = defence_clan
					.getOnlineClanMember();
			for (int i = 0; i < defence_clan_member.length; i++) {
				for (String clanName : GetAttackClanList()) {
					defence_clan_member[i].sendPackets(new S_War(3,
							defence_clan_name, clanName));
				}
			}
		}

		String clanList[] = GetAttackClanList();
		for (int j = 0; j < clanList.length; j++) {
			if (clanList[j] != null) {
				L1World.getInstance().broadcastPacketToAll(new S_ServerMessage( // %0血盟%1血盟间战争终结。
						227, defence_clan_name, clanList[j]));
				L1Clan clan = L1World.getInstance().getClan(clanList[j]);
				if (clan != null) {
					L1PcInstance clan_member[] = clan.getOnlineClanMember();
					for (int k = 0; k < clan_member.length; k++) {
						clan_member[k].sendPackets(new S_War(3, clanList[j],
								defence_clan_name));
					}
				}
			}
		}

		_isWarTimerDelete = true;
		delete();
	}

	public void CeaseCastleWar() { // 战争时间满了、防卫侧胜利
		String defence_clan_name = GetDefenceClanName();
		String clanList[] = GetAttackClanList();
		if (defence_clan_name != null) {
			L1World.getInstance().broadcastPacketToAll(new S_ServerMessage( // %0血盟%1血盟战争胜利。
					231, defence_clan_name, clanList[0]));
		}

		L1Clan defence_clan = L1World.getInstance().getClan(defence_clan_name);
		if (defence_clan != null) {
			L1PcInstance defence_clan_member[] = defence_clan
					.getOnlineClanMember();
			for (int i = 0; i < defence_clan_member.length; i++) {
				defence_clan_member[i].sendPackets(new S_War(4,
						defence_clan_name, clanList[0]));
			}
		}

		for (int j = 0; j < clanList.length; j++) {
			if (clanList[j] != null) {
				L1World.getInstance().broadcastPacketToAll(new S_ServerMessage( // %0血盟%1血盟间战争终结。
						227, defence_clan_name, clanList[j]));
				L1Clan clan = L1World.getInstance().getClan(clanList[j]);
				if (clan != null) {
					L1PcInstance clan_member[] = clan.getOnlineClanMember();
					for (int k = 0; k < clan_member.length; k++) {
						clan_member[k].sendPackets(new S_War(3, clanList[j],
								defence_clan_name));
					}
				}
			}
		}

		_isWarTimerDelete = true;
		delete();
	}

	public void DeclareWar(String clan1_name, String clan2_name) { // _血盟_血盟宣战布告。
		if (GetWarType() == 1) { // 攻城战
			RequestCastleWar(1, clan1_name, clan2_name);
		} else { // 模拟战
			RequestSimWar(1, clan1_name, clan2_name);
		}
	}

	public void SurrenderWar(String clan1_name, String clan2_name) { // _血盟_血盟降伏。
		if (GetWarType() == 1) {
			RequestCastleWar(2, clan1_name, clan2_name);
		} else {
			RequestSimWar(2, clan1_name, clan2_name);
		}
	}

	public void CeaseWar(String clan1_name, String clan2_name) { // _血盟_血盟战争终结。
		if (GetWarType() == 1) {
			RequestCastleWar(3, clan1_name, clan2_name);
		} else {
			RequestSimWar(3, clan1_name, clan2_name);
		}
	}

	public void WinWar(String clan1_name, String clan2_name) { // _血盟_血盟战争胜利。
		if (GetWarType() == 1) {
			RequestCastleWar(4, clan1_name, clan2_name);
		} else {
			RequestSimWar(4, clan1_name, clan2_name);
		}
	}

	public boolean CheckClanInWar(String clan_name) { // 战争参加
		boolean ret;
		if (GetDefenceClanName().toLowerCase().equals(clan_name.toLowerCase())) { // 防卫侧
			ret = true;
		} else {
			ret = CheckAttackClan(clan_name); // 攻击侧
		}
		return ret;
	}

	public boolean CheckClanInSameWar(String player_clan_name,
			String target_clan_name) { // 自相手同战争参加（同场合含）
		boolean player_clan_flag;
		boolean target_clan_flag;

		if (GetDefenceClanName().toLowerCase().equals(
				player_clan_name.toLowerCase())) { // 自对防卫侧
			player_clan_flag = true;
		} else {
			player_clan_flag = CheckAttackClan(player_clan_name); // 自对攻击侧
		}

		if (GetDefenceClanName().toLowerCase().equals(
				target_clan_name.toLowerCase())) { // 相手对防卫侧
			target_clan_flag = true;
		} else {
			target_clan_flag = CheckAttackClan(target_clan_name); // 相手对攻击侧
		}

		if (player_clan_flag == true && target_clan_flag == true) {
			return true;
		} else {
			return false;
		}
	}

	public String GetEnemyClanName(String player_clan_name) { // 相手名取得
		String enemy_clan_name = null;
		if (GetDefenceClanName().toLowerCase().equals(
				player_clan_name.toLowerCase())) { // 自防卫侧
			String clanList[] = GetAttackClanList();
			for (int cnt = 0; cnt < clanList.length; cnt++) {
				if (clanList[cnt] != null) {
					enemy_clan_name = clanList[cnt];
					return enemy_clan_name; // 先头名返
				}
			}
		} else { // 自攻击侧
			enemy_clan_name = GetDefenceClanName();
			return enemy_clan_name;
		}
		return enemy_clan_name;
	}

	public void delete() {
		L1World.getInstance().removeWar(this); // 战争削除
	}

	public int GetWarType() {
		return _warType;
	}

	public void SetWarType(int war_type) {
		_warType = war_type;
	}

	public String GetDefenceClanName() {
		return _defenceClanName;
	}

	public void SetDefenceClanName(String defence_clan_name) {
		_defenceClanName = defence_clan_name;
	}

	public void InitAttackClan() {
		_attackClanList.clear();
	}

	public void AddAttackClan(String attack_clan_name) {
		if (!_attackClanList.contains(attack_clan_name)) {
			_attackClanList.add(attack_clan_name);
		}
	}

	public void RemoveAttackClan(String attack_clan_name) {
		if (_attackClanList.contains(attack_clan_name)) {
			_attackClanList.remove(attack_clan_name);
		}
	}

	public boolean CheckAttackClan(String attack_clan_name) {
		if (_attackClanList.contains(attack_clan_name)) {
			return true;
		}
		return false;
	}

	public String[] GetAttackClanList() {
		return _attackClanList.toArray(new String[_attackClanList.size()]);
	}

	public int GetAttackClanListSize() {
		return _attackClanList.size();
	}

	public int GetCastleId() {
		int castle_id = 0;
		if (GetWarType() == 1) { // 攻城战
			L1Clan clan = L1World.getInstance().getClan(GetDefenceClanName());
			if (clan != null) {
				castle_id = clan.getCastleId();
			}
		}
		return castle_id;
	}

	public L1Castle GetCastle() {
		L1Castle l1castle = null;
		if (GetWarType() == 1) { // 攻城战
			L1Clan clan = L1World.getInstance().getClan(GetDefenceClanName());
			if (clan != null) {
				int castle_id = clan.getCastleId();
				l1castle = CastleTable.getInstance().getCastleTable(castle_id);
			}
		}
		return l1castle;
	}
}
