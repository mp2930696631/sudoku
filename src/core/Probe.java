package core;

import core.entity.Cell;

/**
 * @author zehua
 * @date 2021/3/30 7:34
 * <p>
 * 探针，用于改变程序运行方式，主要用于控制递归
 */
public class Probe {

    /*
    只有在其他所有算法都失效的情况下，才会执行递归算法
    程序怎么知道其他算法都失效了呢？使用这个标识：true表示其他算法还能推动数独前进，暂时还不需要使用递归试错算法
     */
    private static boolean FLAG;

    /*
    当在多个候选值中尝试一个值之后，怎么确定该值就是正确的值：使用这个标识
    true：表示当前的这个候选值并不正确，需要继续尝试其他候选值
     */
    private static boolean IS_CONTINUE = true;

    // 表示当前进行试错的单元格
    public static Cell currentTryCell = null;

    public static boolean isFlag() {
        return FLAG;
    }

    public static void setFlag(boolean flag) {
        FLAG = flag;
    }

    public static boolean isIsContinue() {
        return IS_CONTINUE;
    }

    public static void setIsContinue(boolean isContinue) {
        IS_CONTINUE = isContinue;
    }
}
