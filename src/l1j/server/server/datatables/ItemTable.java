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
package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import l1j.server.L1DatabaseFactory;
import l1j.server.data.ItemClass;
import l1j.server.server.IdFactory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Weapon;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.world.L1World;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ItemTable {

	private static final Log _log = LogFactory.getLog(ItemTable.class);

	private static final Map<String, Integer> _armorTypes = new HashMap<String, Integer>();

	private static final Map<String, Integer> _weaponTypes = new HashMap<String, Integer>();

	private static final Map<String, Integer> _weaponId = new HashMap<String, Integer>();

	private static final Map<String, Integer> _materialTypes = new HashMap<String, Integer>();

	private static final Map<String, Integer> _etcItemTypes = new HashMap<String, Integer>();

	private static final Map<String, Integer> _useTypes = new HashMap<String, Integer>();

	private static ItemTable _instance;
	
	private final Map<Integer, L1Item> _allTemplates = new HashMap<Integer,L1Item>();

	//private L1Item _allTemplates[];

	private final Map<Integer, L1EtcItem> _etcitems;

	private final Map<Integer, L1Armor> _armors;

	private final Map<Integer, L1Weapon> _weapons;

	static {

		_etcItemTypes.put("arrow", new Integer(0));
		_etcItemTypes.put("wand", new Integer(1));
		_etcItemTypes.put("light", new Integer(2));
		_etcItemTypes.put("gem", new Integer(3));
		_etcItemTypes.put("totem", new Integer(4));
		_etcItemTypes.put("firecracker", new Integer(5));
		_etcItemTypes.put("potion", new Integer(6));
		_etcItemTypes.put("food", new Integer(7));
		_etcItemTypes.put("scroll", new Integer(8));
		_etcItemTypes.put("questitem", new Integer(9));
		_etcItemTypes.put("spellbook", new Integer(10));
		_etcItemTypes.put("petitem", new Integer(11));
		_etcItemTypes.put("other", new Integer(12));
		_etcItemTypes.put("material", new Integer(13));
		_etcItemTypes.put("event", new Integer(14));
		_etcItemTypes.put("sting", new Integer(15));
		_etcItemTypes.put("treasure_box", new Integer(16));
		
		_useTypes.put("petitem", new Integer(-12)); // 寵物道具
		_useTypes.put("other", new Integer(-11)); // 对读取方法调用无法分类的物品
		_useTypes.put("power", new Integer(-10)); // 加速药水
		_useTypes.put("book", new Integer(-9)); // 技术书
		_useTypes.put("makecooking", new Integer(-8));// 料理书
		_useTypes.put("hpr", new Integer(-7));// 增HP道具
		_useTypes.put("mpr", new Integer(-6));// 增MP道具
		_useTypes.put("none", new Integer(-1)); // 使用不可能
		_useTypes.put("normal", new Integer(0));
		_useTypes.put("weapon", new Integer(1));
		_useTypes.put("armor", new Integer(2));
// _useTypes.put("wand1", new Integer(3));
// _useTypes.put("wand", new Integer(4));
		// 振(C_RequestExtraCommand送)
		_useTypes.put("spell_long", new Integer(5)); // 地面 / 选择(远距离)
		_useTypes.put("ntele", new Integer(6));
		_useTypes.put("identify", new Integer(7));
		_useTypes.put("res", new Integer(8));
		_useTypes.put("home", new Integer(9)); // 傳送回家的卷軸
		_useTypes.put("letter", new Integer(12));
		_useTypes.put("letter_w", new Integer(13));
		_useTypes.put("choice", new Integer(14));
		_useTypes.put("instrument", new Integer(15));
		_useTypes.put("sosc", new Integer(16));
		_useTypes.put("spell_short", new Integer(17)); // 地面 / 选择(近距离)
		_useTypes.put("T", new Integer(18));
		_useTypes.put("cloak", new Integer(19));
		_useTypes.put("glove", new Integer(20));
		_useTypes.put("boots", new Integer(21));
		_useTypes.put("helm", new Integer(22));
		_useTypes.put("ring", new Integer(23));
		_useTypes.put("amulet", new Integer(24));
		_useTypes.put("shield", new Integer(25));
		//_useTypes.put("guarder", new Integer(25));// 臂甲
		_useTypes.put("dai", new Integer(26));
		_useTypes.put("zel", new Integer(27));
		_useTypes.put("blank", new Integer(28));
		_useTypes.put("btele", new Integer(29));
		_useTypes.put("spell_buff", new Integer(30)); // 选择(远距离)
														// Ctrl押飞？
		_useTypes.put("ccard", new Integer(31));
		_useTypes.put("ccard_w", new Integer(32));
		_useTypes.put("vcard", new Integer(33));
		_useTypes.put("vcard_w", new Integer(34));
		_useTypes.put("wcard", new Integer(35));
		_useTypes.put("wcard_w", new Integer(36));
		_useTypes.put("belt", new Integer(37));
		// _useTypes.put("spell_long2", new Integer(39)); // 地面 / 选择(远距离)
		// 5同？
		_useTypes.put("earring", new Integer(40));
		_useTypes.put("fishing_rod", new Integer(42));
		
		_useTypes.put("aidr", new Integer(43));// 3.63TW辅助1
		_useTypes.put("vipring", new Integer(44));//VIP戒指
		_useTypes.put("vipring2", new Integer(45));//VIP戒指
		_useTypes.put("aidr2", new Integer(48));// 3.5TW辅助右下

		_armorTypes.put("none", new Integer(0));
		_armorTypes.put("helm", new Integer(1));
		_armorTypes.put("armor", new Integer(2));
		_armorTypes.put("T", new Integer(3));
		_armorTypes.put("cloak", new Integer(4));
		_armorTypes.put("glove", new Integer(5));
		_armorTypes.put("boots", new Integer(6));
		_armorTypes.put("shield", new Integer(7));
		_armorTypes.put("amulet", new Integer(8));
		_armorTypes.put("ring", new Integer(9));
		_armorTypes.put("belt", new Integer(10));
		_armorTypes.put("ring2", new Integer(11));
		_armorTypes.put("earring", new Integer(12));
		//_armorTypes.put("guarder", new Integer(13));// 臂甲
		
		_armorTypes.put("aidr", new Integer(15)); // 副助道具
		_armorTypes.put("vipring", new Integer(16));//VIP戒指
		_armorTypes.put("vipring2", new Integer(17));//VIP戒指
		_armorTypes.put("aidr2", new Integer(18)); // 副助道具

		_weaponTypes.put("sword", new Integer(1));
		_weaponTypes.put("dagger", new Integer(2));
		_weaponTypes.put("tohandsword", new Integer(3));
		_weaponTypes.put("bow", new Integer(4));
		_weaponTypes.put("spear", new Integer(5));
		_weaponTypes.put("blunt", new Integer(6));
		_weaponTypes.put("staff", new Integer(7));
		_weaponTypes.put("throwingknife", new Integer(8));
		_weaponTypes.put("arrow", new Integer(9));
		_weaponTypes.put("gauntlet", new Integer(10));
		_weaponTypes.put("claw", new Integer(11));
		_weaponTypes.put("edoryu", new Integer(12));
		_weaponTypes.put("singlebow", new Integer(13));
		_weaponTypes.put("singlespear", new Integer(14));
		_weaponTypes.put("tohandblunt", new Integer(15));
		_weaponTypes.put("tohandstaff", new Integer(16));
		_weaponTypes.put("kiringku", new Integer(17));// 奇古兽(单手)
		_weaponTypes.put("chainsword", new Integer(18));// 锁链剑(单手)

		_weaponId.put("sword", new Integer(4));
		_weaponId.put("dagger", new Integer(46));
		_weaponId.put("tohandsword", new Integer(50));
		_weaponId.put("bow", new Integer(20));
		_weaponId.put("blunt", new Integer(11));
		_weaponId.put("spear", new Integer(24));
		_weaponId.put("staff", new Integer(40));
		_weaponId.put("throwingknife", new Integer(2922));
		_weaponId.put("arrow", new Integer(66));
		_weaponId.put("gauntlet", new Integer(62));
		_weaponId.put("claw", new Integer(58));
		_weaponId.put("edoryu", new Integer(54));
		_weaponId.put("singlebow", new Integer(20));
		_weaponId.put("singlespear", new Integer(24));
		_weaponId.put("tohandblunt", new Integer(11));
		_weaponId.put("tohandstaff", new Integer(40));
		_weaponId.put("kiringku", new Integer(58));// 奇古兽
		_weaponId.put("chainsword", new Integer(24));// 锁链剑

		_materialTypes.put("none", new Integer(0));
		_materialTypes.put("liquid", new Integer(1));
		_materialTypes.put("web", new Integer(2));
		_materialTypes.put("vegetation", new Integer(3));
		_materialTypes.put("animalmatter", new Integer(4));
		_materialTypes.put("paper", new Integer(5));
		_materialTypes.put("cloth", new Integer(6));
		_materialTypes.put("leather", new Integer(7));
		_materialTypes.put("wood", new Integer(8));
		_materialTypes.put("bone", new Integer(9));
		_materialTypes.put("dragonscale", new Integer(10));
		_materialTypes.put("iron", new Integer(11));
		_materialTypes.put("steel", new Integer(12));
		_materialTypes.put("copper", new Integer(13));
		_materialTypes.put("silver", new Integer(14));
		_materialTypes.put("gold", new Integer(15));
		_materialTypes.put("platinum", new Integer(16));
		_materialTypes.put("mithril", new Integer(17));
		_materialTypes.put("blackmithril", new Integer(18));
		_materialTypes.put("glass", new Integer(19));
		_materialTypes.put("gemstone", new Integer(20));
		_materialTypes.put("mineral", new Integer(21));
		_materialTypes.put("oriharukon", new Integer(22));
	}

	public static ItemTable getInstance() {
		if (_instance == null) {
			_instance = new ItemTable();
		}
		return _instance;
	}

	private ItemTable() {
		_etcitems = allEtcItem();
		_weapons = allWeapon();
		_armors = allArmor();
		buildFastLookupTable();
	}

	private Map<Integer, L1EtcItem> allEtcItem() {
		Map<Integer, L1EtcItem> result = new HashMap<Integer, L1EtcItem>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1EtcItem item = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from etcitem");

			rs = pstm.executeQuery();
			while (rs.next()) {
				item = new L1EtcItem();
				final int itemid = rs.getInt("item_id");
				item.setItemId(itemid);
				item.setName(rs.getString("name"));
				item.setNameId(rs.getString("name_id"));
				final String classname = rs.getString("classname");
				item.setClassname(classname);
				item.setType((_etcItemTypes
						.get(rs.getString("item_type"))).intValue());
				item.setUseType(_useTypes
						.get(rs.getString("use_type")).intValue());
// item.setType1(0); // 使
				item.setType2(0);
				item.setMaterial((_materialTypes
						.get(rs.getString("material"))).intValue());
				item.setWeight(rs.getInt("weight"));
				item.setGfxId(rs.getInt("invgfx"));
				item.setGroundGfxId(rs.getInt("grdgfx"));
				item.setItemDescId(rs.getInt("itemdesc_id"));
				item.setMinLevel(rs.getInt("min_lvl"));
				item.setMaxLevel(rs.getInt("max_lvl"));
				item.setBless(rs.getInt("bless"));
				item.setTradable(rs.getInt("trade") == 0 ? true : false);
				item.setDmgSmall(rs.getInt("dmg_small"));
				item.setDmgLarge(rs.getInt("dmg_large"));
				item.set_stackable(rs.getInt("stackable") == 1 ? true : false);
				item.setMaxChargeCount(rs.getInt("max_charge_count"));
				item.set_locx(rs.getInt("locx"));
				item.set_locy(rs.getInt("locy"));
				item.set_mapid(rs.getShort("mapid"));
				item.set_delayid(rs.getInt("delay_id"));
				item.set_delaytime(rs.getInt("delay_time"));
				item.set_delayEffect(rs.getInt("delay_effect"));
				item.setFoodVolume(rs.getInt("food_volume"));
				item.setCantDelete(rs.getInt("cant_delete") == 1 ? true : false);
				item.setToBeSavedAtOnce(
						(rs.getInt("save_at_once") == 1) ? true : false);
				item.setBroad(rs.getInt("broad") == 1 ? true : false);
				ItemClass.get().addList(itemid, classname, 0);
				result.put(new Integer(item.getItemId()), item);
			}
		} catch (NullPointerException e) {
			_log.info(new StringBuilder()
					.append(item.getName())
					.append("(" + item.getItemId() + ")")
					.append("读取失败。").toString());
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}

	private Map<Integer, L1Weapon> allWeapon() {
		Map<Integer, L1Weapon> result = new HashMap<Integer, L1Weapon>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Weapon weapon = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from weapon");

			rs = pstm.executeQuery();
			while (rs.next()) {
				weapon = new L1Weapon();
				final int itemid = rs.getInt("item_id");
				weapon.setItemId(itemid);
				weapon.setName(rs.getString("name"));
				final String classname = rs.getString("classname");
				weapon.setClassname(classname);
				weapon.setNameId(rs.getString("name_id"));
				weapon.setType((_weaponTypes
						.get(rs.getString("type"))).intValue());
				weapon.setType1((_weaponId
						.get(rs.getString("type"))).intValue());
				weapon.setType2(1);
				weapon.setUseType(1);
				weapon.setMaterial((_materialTypes
						.get(rs.getString("material"))).intValue());
				weapon.setWeight(rs.getInt("weight"));
				weapon.setGfxId(rs.getInt("invgfx"));
				weapon.setGroundGfxId(rs.getInt("grdgfx"));
				weapon.setItemDescId(rs.getInt("itemdesc_id"));
				weapon.setDmgSmall(rs.getInt("dmg_small"));
				weapon.setDmgLarge(rs.getInt("dmg_large"));
				weapon.setRange(rs.getInt("range"));
				weapon.set_safeenchant(rs.getInt("safenchant"));
				weapon.setUseRoyal(rs.getInt("use_royal") == 0 ? false : true);
				weapon.setUseKnight(rs.getInt("use_knight") == 0
						? false : true);
				weapon.setUseElf(rs.getInt("use_elf") == 0 ? false : true);
				weapon.setUseMage(rs.getInt("use_mage") == 0 ? false : true);
				weapon.setUseDarkelf(rs.getInt("use_darkelf") == 0
						? false : true);
				weapon.setUseDragonknight(rs.getInt("use_dragonknight") == 0 ? false
						: true);
				weapon.setUseIllusionist(rs.getInt("use_illusionist") == 0 ? false
						: true);
				weapon.setHitModifier(rs.getInt("hitmodifier"));
				weapon.setDmgModifier(rs.getInt("dmgmodifier"));
				weapon.set_addstr(rs.getByte("add_str"));
				weapon.set_adddex(rs.getByte("add_dex"));
				weapon.set_addcon(rs.getByte("add_con"));
				weapon.set_addint(rs.getByte("add_int"));
				weapon.set_addwis(rs.getByte("add_wis"));
				weapon.set_addcha(rs.getByte("add_cha"));
				weapon.set_addhp(rs.getInt("add_hp"));
				weapon.set_addmp(rs.getInt("add_mp"));
				weapon.set_addhpr(rs.getInt("add_hpr"));
				weapon.set_addmpr(rs.getInt("add_mpr"));
				weapon.set_addsp(rs.getInt("add_sp"));
				weapon.set_mdef(rs.getInt("m_def"));
				weapon.set_double_dmg_chance(rs.getInt("double_dmg_chance"));
				weapon.setMagicDmgModifier(rs.getInt("magicdmgmodifier"));
				weapon.set_canbedmg(rs.getInt("canbedmg"));
				weapon.setMinLevel(rs.getInt("min_lvl"));
				weapon.setMaxLevel(rs.getInt("max_lvl"));
				weapon.setBless(rs.getInt("bless"));
				weapon.setCantDelete(rs.getInt("cant_delete") == 1 ? true : false);
				weapon.setTradable(rs.getInt("trade") == 0 ? true : false);
				weapon.setHasteItem(rs.getInt("haste_item") == 0
						? false : true);
				// 吸魔武器设定 
				weapon.setManaItem(rs.getInt("mana_item") == 0
						? false : true);
				// 吸魔武器设定  end
				weapon.setBroad(rs.getInt("broad") == 1 ? true : false);
				ItemClass.get().addList(itemid, classname, 1);
				result.put(new Integer(weapon.getItemId()), weapon);
			}
		} catch (NullPointerException e) {
			_log.info(new StringBuilder()
					.append(weapon.getName())
					.append("(" + weapon.getItemId() + ")")
					.append("读取失败。").toString());
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
		return result;
	}

	private Map<Integer, L1Armor> allArmor() {
		Map<Integer, L1Armor> result = new HashMap<Integer, L1Armor>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Armor armor = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from armor");

			rs = pstm.executeQuery();
			while (rs.next()) {
				armor = new L1Armor();
				final int itemid = rs.getInt("item_id");
				armor.setItemId(itemid);
				armor.setName(rs.getString("name"));
				final String classname = rs.getString("classname");
				armor.setClassname(classname);
				armor.setNameId(rs.getString("name_id"));
				armor.setType((_armorTypes
						.get(rs.getString("type"))).intValue());
// armor.setType1((_armorId
// .get(rs.getString("armor_type"))).intValue()); // 使
				armor.setType2(2);
				armor.setUseType((_useTypes
						.get(rs.getString("type"))).intValue());
				armor.setMaterial((_materialTypes
						.get(rs.getString("material"))).intValue());
				armor.setWeight(rs.getInt("weight"));
				armor.setGfxId(rs.getInt("invgfx"));
				armor.setGroundGfxId(rs.getInt("grdgfx"));
				armor.setItemDescId(rs.getInt("itemdesc_id"));
				armor.set_ac(rs.getInt("ac"));
				armor.set_safeenchant(rs.getInt("safenchant"));
				armor.setUseRoyal(rs.getInt("use_royal") == 0 ? false : true);
				armor.setUseKnight(rs.getInt("use_knight") == 0 ? false : true);
				armor.setUseElf(rs.getInt("use_elf") == 0 ? false : true);
				armor.setUseMage(rs.getInt("use_mage") == 0 ? false : true);
				armor.setUseDarkelf(rs.getInt("use_darkelf") == 0 ? false
						: true);
				armor.setUseDragonknight(rs.getInt("use_dragonknight") == 0 ? false
						: true);
				armor.setUseIllusionist(rs.getInt("use_illusionist") == 0 ? false
						: true);
				armor.set_addstr(rs.getByte("add_str"));
				armor.set_addcon(rs.getByte("add_con"));
				armor.set_adddex(rs.getByte("add_dex"));
				armor.set_addint(rs.getByte("add_int"));
				armor.set_addwis(rs.getByte("add_wis"));
				armor.set_addcha(rs.getByte("add_cha"));
				armor.set_addhp(rs.getInt("add_hp"));
				armor.set_addmp(rs.getInt("add_mp"));
				armor.set_addhpr(rs.getInt("add_hpr"));
				armor.set_addmpr(rs.getInt("add_mpr"));
				armor.set_addsp(rs.getInt("add_sp"));
				armor.setMinLevel(rs.getInt("min_lvl"));
				armor.setMaxLevel(rs.getInt("max_lvl"));
				armor.set_mdef(rs.getInt("m_def"));
				armor.setDamageReduction(rs.getInt("damage_reduction"));
				armor.setWeightReduction(rs.getInt("weight_reduction"));
				armor.setBowHitRate(rs.getInt("bow_hit_rate"));
				armor.setHasteItem(rs.getInt("haste_item") == 0 ? false : true);
				armor.setBless(rs.getInt("bless"));
				armor.setCantDelete(rs.getInt("cant_delete") == 1 ? true : false);
				armor.setTradable(rs.getInt("trade") == 0 ? true : false);
				armor.set_defense_earth(rs.getInt("defense_earth"));
				armor.set_defense_water(rs.getInt("defense_water"));
				armor.set_defense_wind(rs.getInt("defense_wind"));
				armor.set_defense_fire(rs.getInt("defense_fire"));
				armor.set_regist_stan(rs.getInt("regist_stan"));
				armor.set_regist_stone(rs.getInt("regist_stone"));
				armor.set_regist_sleep(rs.getInt("regist_sleep"));
				armor.set_regist_freeze(rs.getInt("regist_freeze"));
				armor.setHitModifier(rs.getInt("hit_modifier"));
				armor.setDmgModifier(rs.getInt("dmg_modifier"));
				armor.setBroad(rs.getInt("broad") == 1 ? true : false);
				ItemClass.get().addList(itemid, classname, 2);
				result.put(new Integer(armor.getItemId()), armor);
			}
		} catch (NullPointerException e) {
			_log.info(new StringBuilder()
					.append(armor.getName())
					.append("(" + armor.getItemId() + ")")
					.append("读迂失败。").toString());
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
		return result;
	}

	private void buildFastLookupTable() {
		_allTemplates.putAll(_weapons);
	    _allTemplates.putAll(_armors);
	    _allTemplates.putAll(_etcitems);
	}

	public L1Item getTemplate(int id) {
		return _allTemplates.get(id);
	}

	public L1ItemInstance createItem(int itemId) {
		L1Item temp = getTemplate(itemId);
		if (temp == null) {
			return null;
		}
		L1ItemInstance item = new L1ItemInstance();
		item.setId(IdFactory.getInstance().nextId());
		item.setItem(temp);
		item.setBless(temp.getBless());
		L1World.getInstance().storeWorldObject(item);
		return item;
	}

	public int findItemIdByName(String name) {
		int itemid = 0;
		for (L1Item item : _allTemplates.values()) {
			if (item != null && item.getName().equals(name)) {
				itemid = item.getItemId();
				break;
			}
		}
		return itemid;
	}

	public int findItemIdByNameWithoutSpace(String name) {
		int itemid = 0;
		for (L1Item item : _allTemplates.values()) {
			if (item != null && item.getName().replace(" ", "").equals(name)) {
				itemid = item.getItemId();
				break;
			}
		}
		return itemid;
	}
}
