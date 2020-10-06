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
package l1j.william;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;

import l1j.william.ItemMagic;
import l1j.william.L1WilliamSystemMessage;

// Referenced classes of package l1j.server.server.model:
// L1PcInstance

public class L1WilliamItemMagic {


	private int _item_id;

	private int _checkClass;

	private int _checkItem;

	private int _skill_id;
	
	private int _removeItem;

	public L1WilliamItemMagic(int item_id, int checkClass , int checkItem, int skill_id, int removeItem) {

		_item_id = item_id;
		_checkClass = checkClass;
		_checkItem = checkItem;
		_skill_id = skill_id;
		_removeItem = removeItem;
	}

	public int getItemId() {
		return _item_id;
	}

	public int getCheckClass() {
		return _checkClass;
	}

	public int getCheckItem() {
		return _checkItem;
	}

	public int getSkillId() {
		return _skill_id;
	}

	public int getRemoveItem() {
		return _removeItem;
	}

	public static int checkItemId(int itemId) { // 判断有没有这个编号的道具
		L1WilliamItemMagic Item_Magic = ItemMagic.getInstance().getTemplate(itemId);

		if (Item_Magic == null) {
			return 0;
		}

		int item_id = Item_Magic.getItemId();
		return item_id;
	}
}
