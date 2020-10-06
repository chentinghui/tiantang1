package l1j.server.server.utils;

/**
 * 随机数
 */
public class Random {

    /**
     * 最高 32767 [0x7fff]
     */
    private static final int _max = Short.MAX_VALUE;

    private static int _idx = 0;

    private static double[] _value = new double[_max + 1];

    static {
        for (_idx = 0; _idx < _max + 1; _idx++) {
            _value[_idx] = (Math.random() + Math.random() + Math.random()
                    + Math.random() + Math.random()) % 1.0;
        }
    }

    /** 随机布林值 */
    public static boolean nextBoolean() {
        return (nextInt(2) == 1);
    }

    /** 随机位元 */
    public static byte nextByte() {
        return (byte) nextInt(256);
    }

    /** 取得整数 */
    public static int nextInt(final int n) {
        _idx = _idx & _max;
        return (int) (_value[_idx++] * n);
    }

    /** 取得整数+偏移 */
    public static int nextInt(final int n, final int offset) {
        _idx = _idx & _max;
        return offset + (int) (_value[_idx++] * n);
    }

    /** 随机长整数 */
    public static long nextLong() {
        final long l = nextInt(Integer.MAX_VALUE) << 32 + nextInt(Integer.MAX_VALUE);
        return l;
    }
}
