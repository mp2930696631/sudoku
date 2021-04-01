package core;

import arithmetic.ArithmeticInterf;
import arithmetic.recursionTry.RecursionTry;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author zehua
 * @date 2021/3/30 7:35
 * <p>
 * 算法执行器
 * 在所有数独的算法中，可以分为两类
 * 1、摒除法，就是减少候选值的算法
 * 2、唯一法，就是可以唯一确定某个单元格的值的算法
 */
public class ArithmeticExecutor {
    // 用于存放排除候选值的算法对象
    private static ArrayList<ArithmeticInterf> exclusionArithmetic = new ArrayList<>();
    // 用于存放可以唯一确定一个候选值的算法对象
    private static ArrayList<ArithmeticInterf> uniqueArithmetic = new ArrayList<>();

    private static RecursionTry recursionTry = new RecursionTry();

    public static void run() {
        while (Utils.tlData.get().TODOS.size() != 0) {
            /*
            首先执行唯一算法，然后根据唯一算法的结果判断是否需要运行其他摒除算法
            例如：
            1、显式唯一候选者算法（BaseArithmetic）
            2、隐式唯一候选者算法
             */
            boolean run = true;
            final Iterator<ArithmeticInterf> uniqueIterator = uniqueArithmetic.iterator();
            while (uniqueIterator.hasNext()) {
                final ArithmeticInterf next = uniqueIterator.next();
                next.beforeRun();
                run = next.run() && run;
                next.afterRun();
            }

            /*
            如果基础算法跑不出来，再执行其他的算法
            例如：
            1、候选数对删减法(显式)
            2、候选数对删减法(隐式)
            3、三链数删减法(显式)
            4、三链数删减法(隐式)
            5、四角对角线法则/矩形删除法（X-Wing）
            6、区块摒除法
            7、剑鱼算法（三链列删减法）
             */
            if (!run) {
                final Iterator<ArithmeticInterf> exclusionIterator = exclusionArithmetic.iterator();
                while (exclusionIterator.hasNext()) {
                    final ArithmeticInterf next = exclusionIterator.next();
                    next.beforeRun();
                    next.run();
                    next.afterRun();
                }

                // 这个是为递归试错准备的结束条件
                // 当尝试的值是的最后某个空格的候选值为空，表示该值并不是正确的值，需要尝试其他候选值
                final boolean impossible = Utils.isImpossible();
                if (impossible) {
                    String str = "注：" + Probe.currentTryCell.toString() + "的值可能并不等于" + Probe.currentTryCell.getValue()
                            + ", 需要尝试其他候选值，如果候选值已全部被尝试，程序会返回上一个递归入口的单元格，并尝试对应单元格的其他候选值\n";
                    Utils.write(str);
                    return;
                }
            }

            if (Config.using_recursion_try) {
                // 需要递归试错了
                if (Utils.tlData.get().TODOS.size() != 0 && !Probe.isFlag()) {
                    recursionTry.beforeRun();
                    recursionTry.run();
                    recursionTry.afterRun();
                }
            } else {
                if (Utils.tlData.get().TODOS.size() != 0 && !Probe.isFlag()) {
                    System.out.println("凉了...，需要进行递归试错才能解出此题，请修改配置文件");
                    return;
                }
            }
        }
    }

    public static void addExclusionArithmetic(ArithmeticInterf ai) {
        exclusionArithmetic.add(ai);
    }

    public static void addUniqueArithmetic(ArithmeticInterf ai) {
        uniqueArithmetic.add(ai);
    }
}
