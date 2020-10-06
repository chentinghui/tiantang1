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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.william.L1WilliamItemSummon;

public class ItemSummon {
	private static final Log _log = LogFactory.getLog(ItemSummon.class);


	private static ItemSummon _instance;

	private final HashMap<Integer, L1WilliamItemSummon> _itemIdIndex
			= new HashMap<Integer, L1WilliamItemSummon>();

	public static ItemSummon getInstance() {
		if (_instance == null) {
			_instance = new ItemSummon();
		}
		return _instance;
	}

	private ItemSummon() {
		loadItemSummon();
	}

	private void loadItemSummon() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM william_item_summon");
			rs = pstm.executeQuery();
			fillItemSummon(rs);
		} catch (SQLException e) {
			_log.info("error while creating william_item_summon table",
					e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void fillItemSummon(ResultSet rs) throws SQLException {
		while (rs.next()) {
			int item_id = rs.getInt("item_id");
			int checkLevel = rs.getInt("checkLevel");
			int checkClass = rs.getInt("checkClass");
			int checkItem = rs.getInt("checkItem");
			int hpConsume = rs.getInt("hpConsume");
			int mpConsume = rs.getInt("mpConsume");
			int material = rs.getInt("material");
			int material_count = rs.getInt("material_count");
			int summon_id = rs.getInt("summon_id");
			int summonCost = rs.getInt("summonCost");
			int onlyOne = rs.getInt("onlyOne");
			int removeItem = rs.getInt("removeItem");
			
			
			L1WilliamItemSummon Item_Summon = new L1WilliamItemSummon(item_id, checkLevel, checkClass, checkItem
				, hpConsume, mpConsume, material, material_count, summon_id, summonCost, onlyOne, removeItem);
			_itemIdIndex.put(item_id, Item_Summon);
		}
	}

	public L1WilliamItemSummon getTemplate(int itemId) {
		return _itemIdIndex.get(itemId);
	}
}
