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

package l1j.server.server.clientpackets;



import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.mina.LineageClient;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.world.L1World;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_SelectList extends ClientBasePacket {

	private static final String C_SELECT_LIST = "[C] C_SelectList";


	public C_SelectList(byte abyte0[], LineageClient _client) {
		super(abyte0);
		// 每来。
		int itemObjectId = readD();
		int npcObjectId = readD();
		L1PcInstance pc = _client.getActiveChar();

		if (npcObjectId != 0) { // 武器修理
			L1Object obj = L1World.getInstance().findObject(npcObjectId);
			if (obj != null) {
				if (obj instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					int difflocx = Math.abs(pc.getX() - npc.getX());
					int difflocy = Math.abs(pc.getY() - npc.getY());
					// 3以上离场合无效
					if (difflocx > 3 || difflocy > 3) {
						return;
					}
				}
			}

			L1PcInventory pcInventory = pc.getInventory();
			L1ItemInstance item = pcInventory.getItem(itemObjectId);
			int cost = item.get_durability() * 200;
			if (!pc.getInventory().consumeItem(L1ItemId.ADENA, cost)) {
				return;
			}
			item.set_durability(0);
			pcInventory.updateItem(item, L1PcInventory.COL_DURABILITY);
		} else { // 引出
			int petCost = 0;
			Object[] petList = pc.getPetList().values().toArray();
			for (Object pet : petList) {
				petCost += ((L1NpcInstance) pet).getPetcost();
			}
			int charisma = pc.getCha();
			if (pc.isCrown()) { // 君主
				charisma += 6;
			} else if (pc.isElf()) { // 
				charisma += 12;
			} else if (pc.isWizard()) { // WIZ
				charisma += 6;
			} else if (pc.isDarkelf()) { // DE
				charisma += 6;
			}
			charisma -= petCost;
			int petCount = charisma / 6;
			if (petCount <= 0) {
				return;
			}

			L1Pet l1pet = PetTable.getInstance().getTemplate(itemObjectId);
			if (l1pet != null) {
				L1Npc npcTemp = NpcTable.getInstance().getTemplate(
						l1pet.get_npcid());
				L1PetInstance pet = new L1PetInstance(npcTemp, pc, l1pet);
				pet.setPetcost(6);
			}
		}
	}

	@Override
	public String getType() {
		return C_SELECT_LIST;
	}
}
