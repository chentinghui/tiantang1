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

import java.sql.PreparedStatement;
import java.sql.SQLException;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.SQLUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CharBuffTable {
	private CharBuffTable() {
	}
	
	private static final Log _log = LogFactory.getLog(CharBuffTable.class);

	private static final int[] buffSkill = { 2, 67, // 、
			3, 99, 151, 159, 168, // 、、、、
			43, 54, 1000, 1001, // 、、、
			52, 101,L1SkillId.WIND_WALK, // 、、
			26, 42, 109, 110, // PE:DEX、PE:STR、、
			114, 115, 117, // 、、
			148, 155, 163, // 、、
			149, 156, 166, // 、、
			1002, 1005,L1SkillId.STATUS_ELFBRAVE
			// 新增 
			, L1SkillId.STATUS_STR_POISON, L1SkillId.STATUS_DEX_POISON, l1j.william.New_Id.Skill_AJ_0_3 
			, L1SkillId.COOKING_1_0_N, L1SkillId.COOKING_1_0_S, L1SkillId.COOKING_1_1_N, L1SkillId.COOKING_1_1_S
			, L1SkillId.COOKING_1_2_N, L1SkillId.COOKING_1_2_S, L1SkillId.COOKING_1_3_N, L1SkillId.COOKING_1_3_S
			, L1SkillId.COOKING_1_4_N, L1SkillId.COOKING_1_4_S, L1SkillId.COOKING_1_5_N, L1SkillId.COOKING_1_5_S
			, L1SkillId.COOKING_1_6_N, L1SkillId.COOKING_1_6_S
			// 新增  end
			,L1SkillId.AITIME,L1SkillId.WAITTIME
			,L1SkillId.CHECKAITIME,188
			}; // 、禁止
			

	private static void StoreBuff(int objId, int skillId, int time, int polyId) {
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("INSERT INTO character_buff SET char_obj_id=?, skill_id=?, remaining_time=?, poly_id=?");
			pstm.setInt(1, objId);
			pstm.setInt(2, skillId);
			pstm.setInt(3, time);
			pstm.setInt(4, polyId);
			pstm.execute();
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void DeleteBuff(L1PcInstance pc) {
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("DELETE FROM character_buff WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
	}

	public static void SaveBuff(L1PcInstance pc) {
		for (int skillId : buffSkill) {
			int timeSec = pc.getSkillEffectTimeSec(skillId);
			if (0 < timeSec) {
				int polyId = 0;
				if (skillId == L1SkillId.SHAPE_CHANGE) {
					polyId = pc.getTempCharGfx();
				}
				StoreBuff(pc.getId(), skillId, timeSec, polyId);
			}
		}
		if (pc.isCheckFZ()) {
			StoreBuff(pc.getId(), L1SkillId.CHECKFZ, 2000, pc.getTempCharGfx());
		}
	}

}
