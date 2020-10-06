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

import java.util.Calendar;

import l1j.server.Config;
import l1j.server.server.Opcodes;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_WarTime extends ServerBasePacket {
	
	private static final String S_WAR_TIME = "[S] S_WarTime";	

	public S_WarTime(Calendar cal) {
		// 1997/01/01 17:00基点
		Calendar base_cal = Calendar.getInstance();
		base_cal.set(1997, 0, 1, 17, 0);
		long base_millis = base_cal.getTimeInMillis();
		long millis = cal.getTimeInMillis();
		long diff = millis - base_millis;
		diff -= 1200 * 60 * 1000; // 误差修正
		diff = diff / 60000; // 分以下切舍
		// time1加算3:02（182分）进
		int time = (int) (diff / 182);

		// writeD直前writeC时间调节
		// 0.7倍时间缩
		// 1调整次时间广？
		writeC(Opcodes.S_OPCODE_WARTIME);
		writeH(6); // 数（6以上无效）
		writeS(Config.TIME_ZONE); // 时间后（）内表示文字列
		writeC(0); // ?
		writeC(0); // ?
		writeC(0);
		writeD(time);
		writeC(0);
		writeD(time - 1);
		writeC(0);
		writeD(time - 2);
		writeC(0);
		writeD(time - 3);
		writeC(0);
		writeD(time - 4);
		writeC(0);
		writeD(time - 5);
		writeC(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_WAR_TIME;
	}
}
