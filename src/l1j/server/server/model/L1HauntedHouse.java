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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;

import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1ScarecrowInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_NPCPack;
import l1j.server.server.serverpackets.S_Door;
import l1j.server.server.serverpackets.S_DoorPack;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.world.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1HauntedHouse {

	public static final int STATUS_NONE = 0;
	public static final int STATUS_READY = 1;
	public static final int STATUS_PLAYING = 2;

	private final ArrayList<L1PcInstance> _members =
			new ArrayList<L1PcInstance>();
	private int _hauntedHouseStatus = STATUS_NONE;
	private int _winnersCount = 0;
	private int _goalCount = 0;

	private static L1HauntedHouse _instance;

	public static L1HauntedHouse getInstance() {
		if (_instance == null) {
			_instance = new L1HauntedHouse();
		}
		return _instance;
	}

	private void readyHauntedHouse() {
		setHauntedHouseStatus(STATUS_READY);

		for (int i = 32827; i <=32829; i++) {
			spawnObject(32726, i, (short) 5140);
		}
		for (int j = 32831; j <=32833; j++) {
			spawnObject(32726, j, (short) 5140);
		}

		L1HauntedHouseReadyTimer hhrTimer = new L1HauntedHouseReadyTimer();
		hhrTimer.begin();
	}

	private void startHauntedHouse() {
		setHauntedHouseStatus(STATUS_PLAYING);
		int membersCount = getMembersCount();
		if (membersCount <= 4) {
			setWinnersCount(1);
		} else if (5 >= membersCount && membersCount <= 7) {
			setWinnersCount(2);
		} else if (8 >= membersCount && membersCount <= 10) {
			setWinnersCount(3);
		}
		for (L1PcInstance pc : getMembersArray()) {
			L1SkillUse l1skilluse = new L1SkillUse();
			l1skilluse.handleCommands(pc,
					L1SkillId.CANCELLATION, pc.getId(), pc.getX(), pc.getY(),
					null, 0, L1SkillUse.TYPE_LOGIN);
			L1PolyMorph.doPoly(pc, 2501, 300);
		}

		for (L1Object object : L1World.getInstance().getObject()) {
			if (object instanceof L1DoorInstance) {
				L1DoorInstance door = (L1DoorInstance) object;
				if (door.getMapId() == 5140) {
					door.setOpenStatus(ActionCodes.ACTION_Open);
					door.setDoorPassable(L1DoorInstance.PASS);
					door.broadcastPacket(new S_DoorPack(door));
					door.broadcastPacket(new S_Door(door));
				}
			}
			if (object instanceof L1ScarecrowInstance) {
				L1ScarecrowInstance npc = (L1ScarecrowInstance) object;
				npc.deleteMe();
			}
		}
	}

	public void endHauntedHouse() {
		setHauntedHouseStatus(STATUS_NONE);
		setWinnersCount(0);
		setGoalCount(0);
		for (L1PcInstance pc : getMembersArray()) {
			L1SkillUse l1skilluse = new L1SkillUse();
			l1skilluse.handleCommands(pc,
					L1SkillId.CANCELLATION, pc.getId(), pc.getX(), pc.getY(),
					null, 0, L1SkillUse.TYPE_LOGIN);
			L1Teleport.teleport(pc, 32624, 32813, (short) 4, 5, true);
		}
		clearMembers();
		for (L1Object object : L1World.getInstance().getObject()) {
			if (object instanceof L1DoorInstance) {
				L1DoorInstance door = (L1DoorInstance) object;
				if (door.getMapId() == 5140) {
					door.setOpenStatus(ActionCodes.ACTION_Close);
					door.setDoorPassable(L1DoorInstance.NOT_PASS);
					door.broadcastPacket(new S_DoorPack(door));
					door.broadcastPacket(new S_Door(door));
				}
			}
		}
	}

	public void removeRetiredMembers() {
		L1PcInstance[] temp = getMembersArray();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].getMapId() != 5140) {
				removeMember(temp[i]);
			}
		}
	}

	public void sendMessage(int type, String msg) {
		for (L1PcInstance pc : getMembersArray()) {
			pc.sendPackets(new S_ServerMessage(type, msg));
		}
	}

	public void addMember(L1PcInstance pc) {
		if (!_members.contains(pc)) {
			_members.add(pc);
		}
		if (getMembersCount() == 1 && getHauntedHouseStatus() == STATUS_NONE) {
			readyHauntedHouse();
		}
	}

	public void removeMember(L1PcInstance pc) {
		_members.remove(pc);
	}

	public void clearMembers() {
		_members.clear();
	}

	public boolean isMember(L1PcInstance pc) {
		return _members.contains(pc);
	}

	public L1PcInstance[] getMembersArray() {
		return _members.toArray(new L1PcInstance[_members.size()]);
	}

	public int getMembersCount() {
		return _members.size();
	}

	private void setHauntedHouseStatus(int i) {
		_hauntedHouseStatus = i;
	}

	public int getHauntedHouseStatus() {
		return _hauntedHouseStatus;
	}

	private void setWinnersCount(int i) {
		_winnersCount = i;
	}

	public int getWinnersCount() {
		return _winnersCount;
	}

	public void setGoalCount(int i) {
		_goalCount = i;
	}

	public int getGoalCount() {
		return _goalCount;
	}

	private void spawnObject(int locx, int locy, short mapid) {
		Constructor constructor;
		L1Npc l1npc = NpcTable.getInstance().getTemplate(45001);
		try {
			if (l1npc != null) {
				Object obj = null;
				String s = l1npc.getImpl();
				constructor = Class.forName(
						(new StringBuilder()).append(
								"l1j.server.server.model.Instance.").append(s)
								.append("Instance").toString())
						.getConstructors()[0];
				Object aobj[] = { l1npc };
				L1NpcInstance npc = (L1NpcInstance) constructor
						.newInstance(aobj);
				npc.setId(IdFactory.getInstance().nextId());
				npc.setX(locx);
				npc.setY(locy);
				npc.setHomeX(locx);
				npc.setHomeY(locy);
				npc.setHeading(6);
				npc.setMap(mapid);
				L1World.getInstance().storeWorldObject(npc);
				L1World.getInstance().addVisibleObject(npc);
				npc.broadcastPacket(new S_NPCPack(npc));
			}
		} catch (Exception exception) {
		}
	}



public class L1HauntedHouseReadyTimer extends TimerTask {

	public L1HauntedHouseReadyTimer() {
	}

	@Override
	public void run() {
		this.cancel();
		startHauntedHouse();
		L1HauntedHouseTimer hhTimer = new L1HauntedHouseTimer();
		hhTimer.begin();
	}

	public void begin() {
		Timer timer = new Timer();
		timer.schedule(this, 90000); // 90秒？
	}

}

public class L1HauntedHouseTimer extends TimerTask {

	public L1HauntedHouseTimer() {
	}

	@Override
	public void run() {
		this.cancel();
		endHauntedHouse();
	}

	public void begin() {
		Timer timer = new Timer();
		timer.schedule(this, 300000); // 5分
	}

}

}
