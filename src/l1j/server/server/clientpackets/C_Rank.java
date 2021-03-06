package l1j.server.server.clientpackets;

import java.util.Calendar;
import java.util.Random;

import l1j.server.server.PacketHandler;
import l1j.server.server.mina.LineageClient;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.world.L1World;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Rank extends ClientBasePacket {
	
	private static final Log _log = LogFactory.getLog(C_Rank.class);

    public C_Rank(final byte[] decrypt, final LineageClient client) {
		super(decrypt);
		try {
            // 资料载入
            int data = 0;
            int rank = 0;
            String name = "";

            try {
                data = this.readC();
                rank = this.readC();
                name = this.readS();

            } catch (final Exception e) {
                return;
            }

            final L1PcInstance pc = client.getActiveChar();
            if (pc == null) {
                return;
            }
            //System.out.println(PacketHandler.printData(decrypt, decrypt.length));
            switch (data) {
                case 1:// 阶级
                    this.rank(pc, rank, name);
                    break;

                case 2:// 同盟目录
                case 3:// 加入同盟
                case 4:// 退出同盟
                    break;
                    
                case 5:// 生存呐喊(CTRL+E)
                    if (pc.get_food() >= 225) {
                        if (pc.getWeapon() != null) {
                            final Random random = new Random();
                            long time = pc.get_h_time();
                            final Calendar cal = Calendar.getInstance();
                            long h_time = cal.getTimeInMillis() / 1000;// 换算为秒
                            int n = (int) ((h_time - time) / 60);// 换算为分
                            int addhp = 0;
                            if (n <= 0) {
                                // 1974：还无法使用生存的呐喊。
                            	System.out.println("无法使用 时间不到");
                                pc.sendPackets(new S_ServerMessage(1974));
                            } else if (n >= 1 && n <= 29) {
                                addhp = (int) (pc.getMaxHp() * (n / 100.0D));
                                System.out.println("1-29分内按的回了血：" + addhp);
                            } else if (n >= 30) {
                                int lv = pc.getWeapon().getEnchantLevel();
                                switch (lv) {
                                    case 0:
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                        pc.sendPacketsAll(new S_SkillSound(pc
                                                .getId(), 8907));
                                        pc.sendPacketsAll(new S_SkillSound(pc
                                                .getId(), 8684));
                                        addhp = (int) (pc.getMaxHp() * ((random
                                                .nextInt(20) + 20) / 100.0D));
                                        System.out.println("30分上+：" + addhp + "武器是：" + lv);
                                        break;

                                    case 7:
                                    case 8:
                                        pc.sendPacketsAll(new S_SkillSound(pc
                                                .getId(), 8909));
                                        pc.sendPacketsAll(new S_SkillSound(pc
                                                .getId(), 8685));
                                        addhp = (int) (pc.getMaxHp() * ((random
                                                .nextInt(20) + 30) / 100.0D));
                                        System.out.println("30分上+：" + addhp + "武器是：" + lv);
                                        break;

                                    case 9:
                                    case 10:
                                        pc.sendPacketsAll(new S_SkillSound(pc
                                                .getId(), 8910));
                                        pc.sendPacketsAll(new S_SkillSound(pc
                                                .getId(), 8773));
                                        addhp = (int) (pc.getMaxHp() * ((random
                                                .nextInt(10) + 50) / 100.0D));
                                        System.out.println("30分上+：" + addhp + "武器是：" + lv);
                                        break;

                                    case 11:
                                    default:
                                        pc.sendPacketsAll(new S_SkillSound(pc
                                                .getId(), 8908));
                                        pc.sendPacketsAll(new S_SkillSound(pc
                                                .getId(), 8686));
                                        addhp = (int) (pc.getMaxHp() * (0.7));
                                        System.out.println("30分上+：" + addhp + "武器是：" + lv);
                                        break;
                                }
                            }

                            if (addhp != 0) {
                                pc.set_food((short) 0);
                                pc.sendPackets(new S_PacketBox(
                                        S_PacketBox.FOOD, (short) 0));
                                pc.setCurrentHp(pc.getCurrentHp() + addhp);
                                System.out.println("出错" + addhp);
                            }

                        } else {
                            // 1973：必须装备上武器才可使用。
                            pc.sendPackets(new S_ServerMessage(1973));
                        }
                    }
                    break;

                case 6:// 生存呐喊(Alt+0)
                    if (pc.getWeapon() != null) {
                        int lv = pc.getWeapon().getEnchantLevel();
                        int gfx = 8684;
                        switch (lv) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                                gfx = 8684;
                                break;

                            case 7:
                            case 8:
                                gfx = 8685;
                                break;

                            case 9:
                            case 10:
                                gfx = 8773;
                                break;

                            case 11:
                            default:
                                gfx = 8686;
                                break;
                        }

                        pc.sendPacketsAll(new S_SkillSound(pc.getId(), gfx));

                    } else {
                        // 1973：必须装备上武器才可使用。
                        pc.sendPackets(new S_ServerMessage(1973));
                    }
                    break;
                    
                case 9: // 更新地图剩余时间
                    //pc.sendPackets(new S_PacketBoxMapTimerOut(pc));
                    break;
                default:
                   // System.out.println("C_Rank - 其他: " + data);
                    break;
            }

        } catch (final Exception e) {
            // _log.error(e.getLocalizedMessage(), e);

        } finally {
            this.over();
        }
    }

    private void rank(final L1PcInstance pc, final int rank, final String name) {
        final L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
        final L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
        if (clan == null) {
            return;
        }
        boolean isOK = false;
        // rank 2:一般 3:副君主 4:联盟君主 5:修习骑士 6:守护骑士 7:一般 8:修习骑士 9:守护骑士 10:联盟君主
        if (rank >= 2 && rank <= 10) {
            isOK = true;
        }

        if (!isOK) {
            // 2,149：\f1请输入以下内容: "/阶级 \f0角色名称 阶级[守护骑士, 修习骑士, 一般]\f1"
            pc.sendPackets(new S_ServerMessage(2149));
            return;
        }
        if (pc.isCrown()) { // 君主
            if (pc.getId() != clan.getLeaderId()) { // 血盟主
                // 785 你不再是君主了
                pc.sendPackets(new S_ServerMessage(785));
                return;
            }

        } else {
            // 518 血盟君主才可使用此命令。
            pc.sendPackets(new S_ServerMessage(518));
            return;
        }

        if (targetPc != null) {
            try {
                if (pc.getClanid() == targetPc.getClanid()) {
                    targetPc.setClanRank(rank);
                    targetPc.save();
                    targetPc.sendPackets(new S_PacketBox(
                            S_PacketBox.MSG_RANK_CHANGED, rank,"0000"));

                } else {
                    // 201：\f1%0%d不是你的血盟成员。
                    pc.sendPackets(new S_ServerMessage(201, name));
                    return;
                }

            } catch (final Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }

        } else { // 线上无此人物
        	pc.sendPackets(new S_ServerMessage(109, name));
        }
    }
}
