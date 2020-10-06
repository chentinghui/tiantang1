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

package l1j.server.server.utils;

import java.util.Random;

import l1j.server.Config;

public class CalcStat {

	private static Random rnd = new Random();

	private CalcStat() {

	}

	/**
	 * AC返
	 * 
	 * @param level
	 * @param dex
	 * @return acBonus
	 * 
	 */
	public static int calcAc(int level, int dex) {
		int acBonus = 10;
		if (dex <= 9) {
			acBonus -= level / 8;
		} else if (dex >= 10 && dex <= 12) {
			acBonus -= level / 7;
		} else if (dex >= 13 && dex <= 15) {
			acBonus -= level / 6;
		} else if (dex >= 16 && dex <= 17) {
			acBonus -= level / 5;
		} else if (dex >= 18) {
			acBonus -= level / 4;
		}
		return acBonus;
	}

	/**
	 * 引数WIS对应MR返
	 * 
	 * @param wis
	 * @return mrBonus
	 */
	public static int calcStatMr(int wis) {
		int mrBonus = 0;
		if (wis <= 14) {
			mrBonus = 0;
		} else if (wis >= 15 && wis <= 16) {
			mrBonus = 3;
		} else if (wis == 17) {
			mrBonus = 6;
		} else if (wis == 18) {
			mrBonus = 10;
		} else if (wis == 19) {
			mrBonus = 15;
		} else if (wis == 20) {
			mrBonus = 21;
		} else if (wis == 21) {
			mrBonus = 28;
		} else if (wis == 22) {
			mrBonus = 37;
		} else if (wis == 23) {
			mrBonus = 47;
		} else if (wis == 24) {
			mrBonus = 50;
		} else {
			mrBonus = 50;
		}
		return mrBonus;
	}

	public static int calcDiffMr(int wis, int diff) {
		return calcStatMr(wis + diff) - calcStatMr(wis);
	}

	/**
	 * 各LVUP时HP上升值返
	 * 
	 * @param charType
	 * @param baseMaxHp
	 * @param baseCon
	 * @return HP上升值
	 */
	public static short calcStatHp(int charType, int baseMaxHp, byte baseCon) {
		short randomhp = 0;
		if (baseCon > 15) {
			randomhp = (short) (baseCon - 15);
		}
		if (charType == 0) { // 
			randomhp += (short) (5 + rnd.nextInt(6)); // 初期值分追加

			if (baseMaxHp + randomhp > Config.PRINCE_MAX_HP) {
				randomhp = (short) (Config.PRINCE_MAX_HP - baseMaxHp);
			}
		} else if (charType == 1) { // 
			randomhp += (short) (6 + rnd.nextInt(7)); // 初期值分追加

			if (baseMaxHp + randomhp > Config.KNIGHT_MAX_HP) {
				randomhp = (short) (Config.KNIGHT_MAX_HP - baseMaxHp);
			}
		} else if (charType == 2) { // 
			randomhp += (short) (5 + rnd.nextInt(6)); // 初期值分追加

			if (baseMaxHp + randomhp > Config.ELF_MAX_HP) {
				randomhp = (short) (Config.ELF_MAX_HP - baseMaxHp);
			}
		} else if (charType == 3) { // 
			randomhp += (short) (3 + rnd.nextInt(4)); // 初期值分追加

			if (baseMaxHp + randomhp > Config.WIZARD_MAX_HP) {
				randomhp = (short) (Config.WIZARD_MAX_HP - baseMaxHp);
			}
		} else if (charType == 4) { // 
			randomhp += (short) (5 + rnd.nextInt(6)); // 初期值分追加

			if (baseMaxHp + randomhp > Config.DARKELF_MAX_HP) {
				randomhp = (short) (Config.DARKELF_MAX_HP - baseMaxHp);
			}
		} else if (charType == 5) { // 龙骑
			randomhp += (short) (5 + rnd.nextInt(6)); // 初期值分追加

			if (baseMaxHp + randomhp > Config.DragonKnight_MAX_HP) {
				randomhp = (short) (Config.DragonKnight_MAX_HP - baseMaxHp);
			}
		} else if (charType == 6) { // 幻术
			randomhp += (short) (3 + rnd.nextInt(4)); // 初期值分追加

			if (baseMaxHp + randomhp > Config.Illusionist_MAX_HP) {
				randomhp = (short) (Config.Illusionist_MAX_HP - baseMaxHp);
			}
		}
		if (randomhp < 0) {
			randomhp = 0;
		}
		return randomhp;
	}

	/**
	 * 各LVUP时MP上升值返
	 * 
	 * @param charType
	 * @param baseMaxMp
	 * @param baseWis
	 * @return MP上升值
	 */
	public static short calcStatMp(int charType, int baseMaxMp, byte baseWis) {
		int randommp = 0;
		int seed = 0;
		if (baseWis <= 9) {
			seed = -2;
		} else if (baseWis == 10 || baseWis == 11) {
			seed = -1;
		} else if (baseWis >= 12 && baseWis <= 14) {
			seed = 0;
		} else if (baseWis >= 15 && baseWis <= 17) {
			seed = 2;
		} else if (baseWis == 18 || baseWis == 19) {
			seed = 3;
		} else if (baseWis == 20) {
			seed = 4;
		} else if (baseWis >= 21 && baseWis <= 23) {
			seed = 5;
		} else if (baseWis == 24) {
			seed = 6;
		} else if (baseWis == 25) {
			seed = 7;
		}
		//修正精神超过25时，魔力增加异常 
		else if (baseWis >= 26) {
			seed = 9;
		}
		//修正精神超过25时，魔力增加异常  end
		randommp = 2 + rnd.nextInt(3 + seed % 2 + (seed / 6) * 2) + seed / 2
				- seed / 6;

		if (charType == 0) { // 
			if (baseMaxMp + randommp > Config.PRINCE_MAX_MP) {
				randommp = Config.PRINCE_MAX_MP - baseMaxMp;
			}
		} else if (charType == 1) { // 
			if (baseWis == 9) {
				randommp -= 1;
			} else {
				randommp = (int) (1.0 * randommp / 2 + 0.5);
			}

			if (baseMaxMp + randommp > Config.KNIGHT_MAX_MP) {
				randommp = Config.KNIGHT_MAX_MP - baseMaxMp;
			}
		} else if (charType == 2) { // 
			randommp = (int) (randommp * 1.5);

			if (baseMaxMp + randommp > Config.ELF_MAX_MP) {
				randommp = Config.ELF_MAX_MP - baseMaxMp;
			}
		} else if (charType == 3) { // 
			randommp *= 2;

			if (baseMaxMp + randommp > Config.WIZARD_MAX_MP) {
				randommp = Config.WIZARD_MAX_MP - baseMaxMp;
			}
		} else if (charType == 4) { // 
			randommp = (int) (randommp * 1.5);

			if (baseMaxMp + randommp > Config.DARKELF_MAX_MP) {
				randommp = Config.DARKELF_MAX_MP - baseMaxMp;
			}
		} else if (charType == 5) { // 
			randommp = (int) (randommp * 1.5);

			if (baseMaxMp + randommp > Config.DragonKnight_MAX_MP) {
				randommp = Config.DragonKnight_MAX_MP - baseMaxMp;
			}
		} else if (charType == 6) { // 
			randommp *= 2;

			if (baseMaxMp + randommp > Config.Illusionist_MAX_MP) {
				randommp = Config.Illusionist_MAX_MP - baseMaxMp;
			}
		}
		if (randommp < 0) {
			randommp = 0;
		}
		return (short) randommp;
	}
}
