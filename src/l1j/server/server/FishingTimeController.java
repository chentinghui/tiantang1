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
package l1j.server.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import l1j.server.server.ActionCodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_Fishing;
import l1j.server.server.serverpackets.S_ServerMessage;

public class FishingTimeController implements Runnable {


	private static FishingTimeController _instance;
	private final List<L1PcInstance> _fishingList =
			new ArrayList<L1PcInstance>();

	public static FishingTimeController getInstance() {
		if (_instance == null) {
			_instance = new FishingTimeController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(300);
				fishing();
			}
		} catch (Exception e1) {
		}
	}

	public void addMember(L1PcInstance pc) {
		if (pc == null || _fishingList.contains(pc)) {
			return;
		}
		_fishingList.add(pc);
	}

	public void removeMember(L1PcInstance pc) {
		if (pc == null || !_fishingList.contains(pc)) {
			return;
		}
		_fishingList.remove(pc);
	}

	private void fishing() {
		if (_fishingList.size() > 0) {
			long currentTime = System.currentTimeMillis();
			for (int i = 0; i < _fishingList.size(); i++) {
				L1PcInstance pc = (L1PcInstance) _fishingList.get(i);
				if (pc.isFishing()) {
					long time = pc.getFishingTime();
					if (currentTime <= (time + 500)
							&& currentTime >= (time - 500)
							&& !pc.isFishingReady()) {
						pc.setFishingReady(true);
						pc.sendPackets(new S_Fishing());
						pc.sendPackets(new S_ServerMessage(1211, "")); // xxx
					} else if (currentTime > (time + 700)) {
						pc.setFishingTime(0);
						pc.setFishingReady(false);
						pc.setFishing(false);
						pc.sendPackets(new S_CharVisualUpdate(pc));
						pc.broadcastPacket(new S_CharVisualUpdate(pc));
						pc.sendPackets(new S_ServerMessage(1163, "")); // 钓终了。
						removeMember(pc);
					}
				}
			}
		}
	}

}
