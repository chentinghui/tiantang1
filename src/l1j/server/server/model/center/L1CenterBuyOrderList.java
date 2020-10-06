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
package l1j.server.server.model.center;

import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javolution.util.FastTable;
import l1j.server.server.templates.L1CenterItem;

class L1CenterBuyOrder {
	private final L1CenterItem _item;
	private final int _count;

	public L1CenterBuyOrder(L1CenterItem item, int count) {
		_item = item;
		_count = Math.max(1, count);
	}

	public L1CenterItem getItem() {
		return _item;
	}

	public int getCount() {
		return _count;
	}
}

public class L1CenterBuyOrderList {
	private static final Log _log = LogFactory.getLog(L1CenterBuyOrderList.class);

	private final L1Center _shop;
	private final List<L1CenterBuyOrder> _list = new FastTable<L1CenterBuyOrder>();
	//private final L1TaxCalculator _taxCalc;

	private int _totalWeight = 0;
	private int _totalPrice = 0;
	//private int _totalPriceTaxIncluded = 0;

	L1CenterBuyOrderList(L1Center shop) {
		_shop = shop;
		//_taxCalc = new L1TaxCalculator(shop.getNpcId());
	}

	public void add(int orderNumber, int count) {
		if (_shop.getSellingItems().size() < orderNumber) {
			return;
		}
		if (count <= 0) {
			return;
		}
		if (count > 1000) {
			return;
		}
		L1CenterItem shopItem = _shop.getSellingItems().get(orderNumber);
		//System.out.println("orderlist1交易数据第一个交易框:" + orderNumber +"数量:"+ count+" 数据接收");
		int price = (int) (shopItem.getPrice() /** Config.RATE_SHOP_SELLING_PRICE*/);
		// オーバーフローチェック
		for (int j = 0; j < count; j++) {
			if (price * j < 0) {
				return;
			}
		}
		//System.out.println("orderlist1交易数据第一个交易框数据接收成功");
		if (_totalPrice < 0) {
			return;
		}
		//System.out.println("orderlist1交易数据第一个交易框数据接收成功");
		_totalPrice += price * count;
		//_totalPriceTaxIncluded += _taxCalc.layTax(price) * count;
		_totalWeight += shopItem.getItem().getWeight() * count
				* shopItem.getPackCount();

		if (shopItem.getItem().isStackable()) {
			_list.add(new L1CenterBuyOrder(shopItem, count
					* shopItem.getPackCount()));
			return;
		}

		for (int i = 0; i < (count * shopItem.getPackCount()); i++) {
			_list.add(new L1CenterBuyOrder(shopItem, 1));
			//System.out.println("orderlist1交易数据第一个交易框数据接收成功");
		}
	}

	List<L1CenterBuyOrder> getList() {
		return _list;
	}

	public int getTotalWeight() {
		return _totalWeight;
	}

	public int getTotalPrice() {
		return _totalPrice;
	}

	/*public int getTotalPriceTaxIncluded() {
		return _totalPriceTaxIncluded;
	}*/

	/*L1TaxCalculator getTaxCalculator() {
		return _taxCalc;
	}*/
}
