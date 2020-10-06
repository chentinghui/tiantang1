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
package l1j.server.server.command.executor;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import l1j.server.server.datatables.IpTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.world.L1World;

public class L1BanIp implements L1CommandExecutor {

	private L1BanIp() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1BanIp();
	}

	// @Override
	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(arg);
			// IPを指定
			String s1 = stringtokenizer.nextToken();

			// add/delを指定(しなくてもOK)
			String s2 = null;
			try {
				s2 = stringtokenizer.nextToken();
			} catch (Exception e) {
			}

			IpTable iptable = IpTable.getInstance();
			boolean isBanned = iptable.isBannedIp(s1);

			for (L1PcInstance tg : L1World.getInstance().getAllPlayers()) {
				if (s1.equals(tg.getNetConnection().getIp())) {
					String msg = new StringBuilder().append("IP:").append(s1)
							.append(" 连线中的角色名称:").append(tg.getName())
							.toString();
					pc.sendPackets(new S_SystemMessage(msg));
				}
			}

			if ("add".equals(s2) && !isBanned) {
				iptable.banIp(s1); // BANリストへIPを加える
				String msg = new StringBuilder().append("IP:").append(s1)
						.append(" 被新增到封锁名单。").toString();
				pc.sendPackets(new S_SystemMessage(msg));
			} else if ("del".equals(s2) && isBanned) {
				if (iptable.liftBanIp(s1)) { // BANリストからIPを削除する
					String msg = new StringBuilder().append("IP:").append(s1)
							.append(" 已从封锁名单中删除。").toString();
					pc.sendPackets(new S_SystemMessage(msg));
				}
			} else {
				// BANの确认
				if (isBanned) {
					String msg = new StringBuilder().append("IP:").append(s1)
							.append(" 已被登记在封锁名单中。").toString();
					pc.sendPackets(new S_SystemMessage(msg));
				} else {
					String msg = new StringBuilder().append("IP:").append(s1)
							.append(" 尚未被登记在封锁名单中。").toString();
					pc.sendPackets(new S_SystemMessage(msg));
				}
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName
					+ " IP [ add | del ]请输入。"));
		}
	}
}
