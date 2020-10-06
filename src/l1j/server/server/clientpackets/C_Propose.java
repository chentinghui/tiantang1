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

package l1j.server.server.clientpackets;

import java.util.logging.Logger;


import l1j.server.server.mina.LineageClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.FaceToFace;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Propose extends ClientBasePacket {

	private static final String C_PROPOSE = "[C] C_Propose";

	public C_Propose(byte abyte0[], LineageClient _client) {
		super(abyte0);
		int c = readC();

		L1PcInstance pc = _client.getActiveChar();
		if (c == 0) { // /propose（/）
			if (pc.isGhost()) {
				return;
			}
			L1PcInstance target = FaceToFace.faceToFace(pc);
			if (target != null) {
				if (pc.getPartnerId() != 0) {
					pc.sendPackets(new S_ServerMessage(657)); // \f1结婚。
					return;
				}
				if (target.getPartnerId() != 0) {
					pc.sendPackets(new S_ServerMessage(658)); // \f1相手结婚。
					return;
				}
				if (pc.get_sex() == target.get_sex()) {
					pc.sendPackets(new S_ServerMessage(661)); // \f1结婚相手异性。
					return;
				}
				if (pc.getX() >= 33974 && pc.getX() <= 33976
						&& pc.getY() >= 33362 && pc.getY() <= 33365
						&& pc.getMapId() == 4 && target.getX() >= 33974
						&& target.getX() <= 33976 && target.getY() >= 33362
						&& target.getY() <= 33365 && target.getMapId() == 4) {
					target.setTempID(pc.getId()); // 相手ID保存
					target.sendPackets(new S_Message_YN(654, pc.getName())); // %0%s结婚。%0结婚？（Y/N）
				}
			}
		} else if (c == 1) { // /divorce（/离婚）
			if (pc.getPartnerId() == 0) {
				pc.sendPackets(new S_ServerMessage(662)); // \f1结婚。
				return;
			}
			pc.sendPackets(new S_Message_YN(653, "")); // 离婚消。离婚望？（Y/N）
		}
	}

	@Override
	public String getType() {
		return C_PROPOSE;
	}
}
