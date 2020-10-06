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

package l1j.server.server.serverpackets;



import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1FollowInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_FollowPack

public class S_FollowPack extends ServerBasePacket {

	private static final String _S__1F_FOLLOWPACK = "[S] S_FollowPack";


	public S_FollowPack(L1FollowInstance pet, L1PcInstance player) {
		/*
		 * int addbyte = 0; int addbyte1 = 1; int addbyte2 = 13; int setting =
		 * 4;
		 */
		writeC(Opcodes.S_OPCODE_CHARPACK);
		writeH(pet.getX());
		writeH(pet.getY());
		writeD(pet.getId());
		writeH(pet.getGfxId()); // SpriteID in List.spr
		writeC(pet.getStatus()); // Modes in List.spr
		writeC(pet.getHeading());
		writeC(pet.getLightSize()); // ??(Bright) - 0~15
		writeC(0); //  - 0:normal, 1:fast
		// 2:slow
		writeD(0);
		writeH(0);
		writeS(pet.getNameId());
		writeS(pet.getTitle());
		writeC(0); //  - 0:mob,item(atk pointer), 1:poisoned(),
		// 2:invisable(), 4:pc, 8:cursed(), 16:brave(),
		// 32:??, 64:??(??), 128:invisable but name
		writeD(0); // ??
		writeS(null); // ??
		writeS(null);
		writeC(0); // ??
		// HP
		/*不显示血条 if (pet.getMaster() != null
				&& pet.getMaster().getId() == player.getId()) {
			writeC(100 * pet.getCurrentHp() / pet.getMaxHp());
		} else {不显示血条 */
			writeC(0xFF);
		/*不显示血条 }不显示血条 */
		writeC(0);
		writeC(pet.getLevel()); // PC = 0, Mon = Lv
		writeC(0);
		writeC(0xFF);
		writeC(0xFF);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return _S__1F_FOLLOWPACK;
	}

}
