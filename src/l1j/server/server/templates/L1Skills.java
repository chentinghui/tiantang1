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

public class L1Skills {

	public static final int ATTR_NONE = 0;

	public static final int ATTR_EARTH = 1;

	public static final int ATTR_FIRE = 2;

	public static final int ATTR_WATER = 4;

	public static final int ATTR_WIND = 8;

	public static final int ATTR_RAY = 16;

	public static final int TYPE_PROBABILITY = 1;

	public static final int TYPE_CHANGE = 2;

	public static final int TYPE_CURSE = 4;

	public static final int TYPE_DEATH = 8;

	public static final int TYPE_HEAL = 16;

	public static final int TYPE_RESTORE = 32;

	public static final int TYPE_ATTACK = 64;

	public static final int TYPE_OTHER = 128;

	public static final int TARGET_TO_ME = 0;

	public static final int TARGET_TO_PC = 1;

	public static final int TARGET_TO_NPC = 2;

	public static final int TARGET_TO_CLAN = 4;

	public static final int TARGET_TO_PARTY = 8;

	public static final int TARGET_TO_PET = 16;

	public static final int TARGET_TO_PLACE = 32;

	private int _skillId;

	public int getSkillId() {
		return _skillId;
	}

	public void setSkillId(int i) {
		_skillId = i;
	}

	private String _name;

	public String getName() {
		return _name;
	}

	public void setName(String s) {
		_name = s;
	}

	private int _skillLevel;

	public int getSkillLevel() {
		return _skillLevel;
	}

	public void setSkillLevel(int i) {
		_skillLevel = i;
	}

	private int _skillNumber;

	public int getSkillNumber() {
		return _skillNumber;
	}

	public void setSkillNumber(int i) {
		_skillNumber = i;
	}

	private int _mpConsume;

	public int getMpConsume() {
		return _mpConsume;
	}

	public void setMpConsume(int i) {
		_mpConsume = i;
	}

	private int _hpConsume;

	public int getHpConsume() {
		return _hpConsume;
	}

	public void setHpConsume(int i) {
		_hpConsume = i;
	}

	private int _itmeConsumeId;

	public int getItemConsumeId() {
		return _itmeConsumeId;
	}

	public void setItemConsumeId(int i) {
		_itmeConsumeId = i;
	}

	private int _itmeConsumeCount;

	public int getItemConsumeCount() {
		return _itmeConsumeCount;
	}

	public void setItemConsumeCount(int i) {
		_itmeConsumeCount = i;
	}

	private int _reuseDelay; // 单位：秒

	public int getReuseDelay() {
		return _reuseDelay;
	}

	public void setReuseDelay(int i) {
		_reuseDelay = i;
	}

	private int _buffDuration; // 单位：秒

	public int getBuffDuration() {
		return _buffDuration;
	}

	public void setBuffDuration(int i) {
		_buffDuration = i;
	}

	private String _target;

	public String getTarget() {
		return _target;
	}

	public void setTarget(String s) {
		_target = s;
	}

	private int _targetTo; // 对像 0:自分 1:PC 2:NPC 4:血盟 8: 16: 32:场所

	public int getTargetTo() {
		return _targetTo;
	}

	public void setTargetTo(int i) {
		_targetTo = i;
	}

	private int _damageValue;

	public int getDamageValue() {
		return _damageValue;
	}

	public void setDamageValue(int i) {
		_damageValue = i;
	}

	private int _damageDice;

	public int getDamageDice() {
		return _damageDice;
	}

	public void setDamageDice(int i) {
		_damageDice = i;
	}

	private int _damageDiceCount;

	public int getDamageDiceCount() {
		return _damageDiceCount;
	}

	public void setDamageDiceCount(int i) {
		_damageDiceCount = i;
	}

	private int _probabilityValue;

	public int getProbabilityValue() {
		return _probabilityValue;
	}

	public void setProbabilityValue(int i) {
		_probabilityValue = i;
	}

	private int _probabilityDice;

	public int getProbabilityDice() {
		return _probabilityDice;
	}

	public void setProbabilityDice(int i) {
		_probabilityDice = i;
	}

	//加入 怪物施法动作设定
	private int _actid;
	
	public int getActid() {
		return _actid;
	}
	
	public void setActid(int i) {
		_actid = i;
	}
	//加入 怪物施法动作设定 end
	
	private int _attr;

	/**
	 * 性返。<br>
	 * 0.无性魔法,1.地魔法,2.火魔法,4.水魔法,8.风魔法,16.光魔法
	 */
	public int getAttr() {
		return _attr;
	}

	public void setAttr(int i) {
		_attr = i;
	}

	private int _type; // 

	/**
	 * 作用种类返。<br>
	 * 1.确率系,2.,4.,8.死,16.治疗,32.复活,64.攻,128.他特殊
	 */
	public int getType() {
		return _type;
	}

	public void setType(int i) {
		_type = i;
	}

	private int _lawful;

	public int getLawful() {
		return _lawful;
	}

	public void setLawful(int i) {
		_lawful = i;
	}

	private int _ranged;

	public int getRanged() {
		return _ranged;
	}

	public void setRanged(int i) {
		_ranged = i;
	}

	private int _area;

	public int getArea() {
		return _area;
	}

	public void setArea(int i) {
		_area = i;
	}

	boolean _isThrough;

	public boolean getIsThrough() {
		return _isThrough;
	}

	public void setIsThrough(int flag) {
		if (flag == 0) {
			_isThrough = false;
		} else {
			_isThrough = true;
		}
	}

	private int _id;

	public int getId() {
		return _id;
	}

	public void setId(int i) {
		_id = i;
	}

	private String _nameId;

	public String getNameId() {
		return _nameId;
	}

	public void setNameId(String s) {
		_nameId = s;
	}

	private int _castGfx;

	public int getCastGfx() {
		return _castGfx;
	}

	public void setCastGfx(int i) {
		_castGfx = i;
	}

	private int _sysmsgIdHappen;

	public int getSysmsgIdHappen() {
		return _sysmsgIdHappen;
	}

	public void setSysmsgIdHappen(int i) {
		_sysmsgIdHappen = i;
	}

	private int _sysmsgIdStop;

	public int getSysmsgIdStop() {
		return _sysmsgIdStop;
	}

	public void setSysmsgIdStop(int i) {
		_sysmsgIdStop = i;
	}

	private int _sysmsgIdFail;

	public int getSysmsgIdFail() {
		return _sysmsgIdFail;
	}

	public void setSysmsgIdFail(int i) {
		_sysmsgIdFail = i;
	}

	boolean _arrowType; // 矢状？(EB,COC等)

	public boolean getArrowType() {
		return _arrowType;
	}

	public void setArrowType(boolean flag) {
		_arrowType = flag;
	}

}
