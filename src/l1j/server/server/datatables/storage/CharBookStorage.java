package l1j.server.server.datatables.storage;

import java.util.ArrayList;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1BookMark;

public interface CharBookStorage {

    /**
     * 初始化载入
     */
    public void load();

    /**
     * 取回保留记忆座标纪录群
     * 
     * @param pc
     */
    public ArrayList<L1BookMark> getBookMarks(final L1PcInstance pc);

    /**
     * 取回保留记忆座标纪录
     * 
     * @param pc
     */
    public L1BookMark getBookMark(final L1PcInstance pc, final int i);

    /**
     * 删除记忆座标
     * 
     * @param pc
     * @param s
     */
    public void deleteBookmark(final L1PcInstance pc, final String s);

    /**
     * 增加记忆座标
     * 
     * @param pc
     * @param s
     */
    public void addBookmark(final L1PcInstance pc, final String s);
}
