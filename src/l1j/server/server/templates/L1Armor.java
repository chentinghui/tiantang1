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
package l1j.server.server.templates;

public class L1Armor extends L1Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public L1Armor() {
	}

	private int _ac = 0; // ● ＡＣ

	@Override
	public int get_ac() {
		return _ac;
	}

	public void set_ac(int i) {
		this._ac = i;
	}

	private int _damageReduction = 0; // ● 轻减

	@Override
	public int getDamageReduction() {
		return _damageReduction;
	}

	public void setDamageReduction(int i) {
		_damageReduction = i;
	}

	private int _weightReduction = 0; // ● 重量轻减

	@Override
	public int getWeightReduction() {
		return _weightReduction;
	}

	public void setWeightReduction(int i) {
		_weightReduction = i;
	}

	private int _bowHitRate = 0; // ● 弓命中率

	@Override
	public int getBowHitRate() {
		return _bowHitRate;
	}

	public void setBowHitRate(int i) {
		_bowHitRate = i;
	}

	private int _defense_water = 0; // ● 水属性防御

	public void set_defense_water(int i) {
		_defense_water = i;
	}

	@Override
	public int get_defense_water() {
		return this._defense_water;
	}

	private int _defense_wind = 0; // ● 风属性防御

	public void set_defense_wind(int i) {
		_defense_wind = i;
	}

	@Override
	public int get_defense_wind() {
		return this._defense_wind;
	}

	private int _defense_fire = 0; // ● 火属性防御

	public void set_defense_fire(int i) {
		_defense_fire = i;
	}

	@Override
	public int get_defense_fire() {
		return this._defense_fire;
	}

	private int _defense_earth = 0; // ● 土属性防御

	public void set_defense_earth(int i) {
		_defense_earth = i;
	}

	@Override
	public int get_defense_earth() {
		return this._defense_earth;
	}

	private int _regist_stan = 0; // ● 耐性

	public void set_regist_stan(int i) {
		_regist_stan = i;
	}

	public int get_regist_stan() {
		return this._regist_stan;
	}

	private int _regist_stone = 0; // ● 石化耐性

	public void set_regist_stone(int i) {
		_regist_stone = i;
	}

	public int get_regist_stone() {
		return this._regist_stone;
	}

	private int _regist_sleep = 0; // ● 睡眠耐性

	public void set_regist_sleep(int i) {
		_regist_sleep = i;
	}

	public int get_regist_sleep() {
		return this._regist_sleep;
	}

	private int _regist_freeze = 0; // ● 冻结耐性

	public void set_regist_freeze(int i) {
		_regist_freeze = i;
	}

	public int get_regist_freeze() {
		return this._regist_freeze;
	}
	
	private int _hitModifier = 0; // ● 命中率补正

	@Override
	public int getHitModifier() {
		return _hitModifier;
	}

	public void setHitModifier(int i) {
		_hitModifier = i;
	}

	private int _dmgModifier = 0; // ● 补正

	@Override
	public int getDmgModifier() {
		return _dmgModifier;
	}

	public void setDmgModifier(int i) {
		_dmgModifier = i;
	}

}