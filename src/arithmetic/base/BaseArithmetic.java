package arithmetic.base;

import arithmetic.ArithmeticInterf;
import core.Config;
import core.Constants;
import core.Probe;
import core.Utils;
import core.entity.Cell;

/**
 * 显式唯一候选者算法（BaseArithmetic）
 *
 * @author zehua
 * @date 2021/3/30 7:36
 * <p>
 * 基础算法，就是通过判断该单元格的候选数字是否唯一来确定单元格的值
 * 也就是显式唯一候选者算法
 */
public class BaseArithmetic implements ArithmeticInterf {
    @Override
    public boolean run() {
        // 递归后跑到这里面来意味着重新开始，需要设置为false
        Probe.setFlag(false);

        while (Utils.tlData.get().TODOS.size() != 0) {
            boolean flag = false;
            final int size = Utils.tlData.get().TODOS.size();
            for (int i = 0; i < size; i++) {
                final Cell cell = Utils.tlData.get().TODOS.get(i);

                final boolean b = doRun(cell);
                // 只要确定了一个单元格的值，就从头再来
                if (b) {
                    flag = true;
                    cell.setValue(cell.getPValue(0));
                    Utils.tlData.get().TODOS.remove(cell);
                    Utils.removePV(new int[]{cell.getValue()}, cell, null);

                    printStep(cell, cell.getValue(), Constants.BASE_ARITHMETIC_NAME);

                    break;
                }
            }

            if (!flag) {
                System.out.println("try to run other arithmetic..");
                return false;
            }
        }

        // 已经得出结果了。不再需要递归
        Probe.setIsContinue(false);
        return true;
    }

    private boolean doRun(Cell cell) {
        return cell.isInsure();
    }

    private void printStep(Cell cell, Integer value, String arithmeticName) {
        if (Config.print_step) {
            StringBuilder sb = new StringBuilder();
            sb.append(Utils.getStepHeader());
            sb.append(cell.toString());
            sb.append("=");
            sb.append(value);
            sb.append(", ");
            sb.append("(" + arithmeticName + ")");
            sb.append("\n");

            Utils.write(sb.toString());

            /*if (Config.print_sudoku_detail) {
                Utils.write(Utils.getResultStr());
            }*/
        }
    }
}
