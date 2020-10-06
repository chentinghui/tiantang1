package l1j.server.data.item_etcitem.shop;

import l1j.server.data.executor.ItemExecutor;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SystemMessage;

public class UserExp extends ItemExecutor{

	/**
	 *
	 */
	private UserExp() {
		// TODO Auto-generated constructor stub
	}

	public static ItemExecutor get() {
		return new UserExp();
	}
	@Override
	public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
		if (item == null) {
			return;
		}
		if (pc.hasSkillEffect(L1SkillId.EXPITEM)) {
			int time = pc.getSkillEffectTimeSec(L1SkillId.EXPITEM);
			pc.sendPackets(new S_SystemMessage("经验加倍时间还剩下"+time+"秒"));
			return;
		}
		pc.getInventory().removeItem(item, 1);
		pc.setSkillEffect(L1SkillId.EXPITEM, sec);
		pc.setItemExp(exp);
		
	}
	private int sec;
	
	private double exp;
	
	@Override
	public void set_set(String[] set) {
		try {
			sec = Integer.parseInt(set[1]);		
		} catch (Exception e) {
			sec =  300;
		}
		sec *= 60;
		sec *= 1000;
		
		try {
			int itemexp = Integer.parseInt(set[2]);	
			exp = itemexp/100;
			if (exp < 1.0) {
				exp = 1.0;
			}
		} catch (Exception e) {
			exp =  1.0;
		}
		
	}

}
