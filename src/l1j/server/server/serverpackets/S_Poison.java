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

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_Poison extends ServerBasePacket {

	/**
	 * 外见毒状态变更际送信构筑
	 * 
	 * @param objId
	 *            外见变ID
	 * @param type
	 *            外见 0 = 通常色, 1 = 绿色, 2 = 灰色
	 */
	public S_Poison(int objId, int type) {
		writeC(Opcodes.S_OPCODE_POISON);
		writeD(objId);

		if (type == 0) { // 通常
			writeC(0);
			writeC(0);
		} else if (type == 1) { // 绿色
			writeC(1);
			writeC(0);
		} else if (type == 2) { // 灰色
			writeC(0);
			writeC(1);
		} else {
			throw new IllegalArgumentException("不正引数。type = " + type);
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_POISON;
	}

	private static final String S_POISON = "[S] S_Poison";
}
