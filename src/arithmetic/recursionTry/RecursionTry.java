package arithmetic.recursionTry;

import arithmetic.ArithmeticInterf;
import core.*;
import core.entity.Cell;
import data.Data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * 递归试错法
 *
 * @author zehua
 * @date 2021/3/30 7:41
 */
public class RecursionTry implements ArithmeticInterf {
    private int num = 1;
    private String prefx = "dataObj";
    private String suffix = ".txt";
    private int limit = 64;

    @Override
    public void beforeRun() {
        System.out.println("RecursionTry.....before");
        Utils.write("RecursionTry.....before\n");
    }

    @Override
    public void afterRun() {
        System.out.println("RecursionTry.....after");
        Utils.write("RecursionTry.....after\n");
    }

    @Override
    public boolean run() {
        final ArrayList<Cell> todos = Utils.tlData.get().TODOS;
        Cell cell = todos.get(0);
        // 找到一个候选数字最少的开始递归
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getPValues().length < cell.getPValues().length) {
                cell = todos.get(i);
            }
        }

        doRun(cell);

        return true;
    }

    private void doRun(Cell cell) {
        Data data = Utils.tlData.get();

        // 使用局部变量保存num的值
        final int numx = num;
        if (numx > limit) {
            String str = "这是试错的最大次数=64：因为数独至少有17个已知数，81-17=64，如果还没有得出答案，表示数独数据有问题，请检查！！\n";
            System.out.println(str);
            Utils.write(str);
            Probe.setFlag(true);
            Probe.setIsContinue(false);
            return;
        }

        final String filePath = getFilePath(num);
        // 序列化data对象
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        num++;

        final Object[] pValues = cell.getPValues();
        for (int i = 0; i < pValues.length; i++) {
            // 没有得出结果，才继续递归
            if (Probe.isIsContinue()) {
                // 反序列化data对象
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                    final Data datax = (Data) ois.readObject();
                    Utils.tlData.set(datax);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Cell newCell = Utils.tlData.get().CELLS[cell.getX()][cell.getY()];
                newCell.setValue((int) pValues[i]);
                Utils.tlData.get().TODOS.remove(newCell);
                Utils.removePV(new int[]{newCell.getValue()}, newCell, null);

                Probe.currentTryCell = newCell;

                printStep(cell, newCell.getValue(), Constants.RECURSION_TRY_NAME);

                ArithmeticExecutor.run();
            }
        }
    }

    public String getFilePath(int num) {
        return Config.recursion_dir + prefx + num + suffix;
    }

    private void printStep(Cell cell, Integer value, String arithmeticName) {
        if (Config.print_step) {
            StringBuilder sb = new StringBuilder();
            sb.append(Utils.getStepHeader());
            sb.append("开始进行递归试错，");
            sb.append(cell.toString());
            sb.append("尝试赋值为");
            sb.append(value);
            sb.append(", ");
            sb.append("(" + arithmeticName + ")");
            sb.append("\n");

            Utils.write(sb.toString());


            Utils.write(Utils.getResultStr());

        }
    }
}
