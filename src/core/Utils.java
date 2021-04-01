package core;

import core.entity.Cell;
import data.Data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zehua
 * @date 2021/3/30 7:34
 */
public class Utils {
    public static ThreadLocal<Data> tlData = new ThreadLocal<>();

    // 输出步骤到文件
    private static BufferedWriter bw;
    public static int step = 1;

    static {
        try {
            if (Config.print_step) {
                bw = new BufferedWriter(new FileWriter(Config.step_sudoku_detail_file_path));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 将步骤写文件
    public static void write(String str) {
        try {
            if (Config.print_step) {
                bw.write(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // BufferedWriter flush
    public static void flush() {
        try {
            if (Config.print_step) {
                bw.flush();
                bw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是递归的结束条件，通过判断当前未填空格中的候选值是否为空
     *
     * @return true: 递归结束，需要重新选择候选值进行再次递归
     */
    public static boolean isImpossible() {
        Data data = tlData.get();
        final ArrayList<Cell> todos = data.TODOS;
        final Iterator<Cell> iterator = todos.iterator();
        while (iterator.hasNext()) {
            final Cell next = iterator.next();

            // 以防万一，还是加上这个判断条件，避免无限递归
            if (next.getValue() != 0) {
                continue;
            }

            if (next.getHashSetPValues().size() == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * 很重要的一个方法
     * 移除某单元格所在的【行、列、小九宫格】中除expects以外所有单元格中的给定的候选值数组
     *
     * @param pvs     需要移除的候选值数组
     * @param cell    基准单元格
     * @param excepts 不需要移除pvs中候选值的单元格
     */
    public static void removePV(int[] pvs, Cell cell, Cell[] excepts) {
        removePV_row(pvs, cell, excepts);
        removePV_column(pvs, cell, excepts);
        removePV_littleSudoku(pvs, cell, excepts);
    }

    /**
     * 根据类型移除pv
     *
     * @param pvs
     * @param cell
     * @param excepts
     * @param type    1：行，2：列，3：小九宫格
     */
    public static boolean removePV_byType(int[] pvs, Cell cell, Cell[] excepts, int type) {
        boolean result = false;
        switch (type) {
            case 1:
                result = Utils.removePV_row(pvs, cell, excepts);
                break;
            case 2:
                result = Utils.removePV_column(pvs, cell, excepts);
                break;
            case 3:
                result = Utils.removePV_littleSudoku(pvs, cell, excepts);
                break;
            default:
                System.out.println("type is invaild");
                break;
        }

        return result;
    }

    /**
     * 很重要的一个方法
     * 移除某单元格所在的【行】中除expects以外所有单元格中的给定的候选值数组
     *
     * @param pvs
     * @param cell
     * @param excepts
     */
    @SuppressWarnings("all")
    public static boolean removePV_row(int[] pvs, Cell cell, Cell[] excepts) {
        final int x = cell.getX();
        final int y = cell.getY();

        boolean result = false;

        for (int i = 0; i < tlData.get().SIZE; i++) {
            if (excepts != null) {
                if (isIncludeCell(tlData.get().CELLS[x][i], excepts)) {
                    continue;
                }
            }
            if (tlData.get().CELLS[x][i].getValue() == 0) {
                final boolean b = tlData.get().CELLS[x][i].removePValues(pvs);
                if (b) {
                    result = true;
                }
            }
        }

        return true;
    }

    /**
     * 很重要的一个方法
     * 移除某单元格所在的【列】中除expects以外所有单元格中的给定的候选值数组
     *
     * @param pvs
     * @param cell
     * @param excepts
     */
    @SuppressWarnings("all")
    public static boolean removePV_column(int[] pvs, Cell cell, Cell[] excepts) {
        final int x = cell.getX();
        final int y = cell.getY();

        boolean result = false;

        for (int i = 0; i < tlData.get().SIZE; i++) {
            if (excepts != null) {
                if (isIncludeCell(tlData.get().CELLS[i][y], excepts)) {
                    continue;
                }
            }
            if (tlData.get().CELLS[i][y].getValue() == 0) {
                final boolean b = tlData.get().CELLS[i][y].removePValues(pvs);
                if (b) {
                    result = true;
                }
            }
        }

        return result;
    }

    /**
     * 很重要的一个方法
     * 移除某单元格所在的【小九宫格】中除expects以外所有单元格中的给定的候选值数组
     *
     * @param pvs
     * @param cell
     * @param excepts
     */
    public static boolean removePV_littleSudoku(int[] pvs, Cell cell, Cell[] excepts) {
        final int x = cell.getX();
        final int y = cell.getY();

        boolean result = false;

        int startX = x / 3;
        int startY = y / 3;
        int startX2 = startX * 3;
        int startY2 = startY * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (excepts != null) {
                    if (isIncludeCell(tlData.get().CELLS[startX2 + i][startY2 + j], excepts)) {
                        continue;
                    }
                }
                if (tlData.get().CELLS[startX2 + i][startY2 + j].getValue() == 0) {
                    final boolean b = tlData.get().CELLS[startX2 + i][startY2 + j].removePValues(pvs);
                    if (b) {
                        result = true;
                    }
                }
            }
        }

        return result;
    }

    /**
     * 打印当前9*9九宫格的所有单元格的实际情况
     * 如有未完成的单元格，对应的输出为0
     */
    public static void printResult() {
        for (int i = 0; i < Utils.tlData.get().SIZE; i++) {
            for (int j = 0; j < Utils.tlData.get().SIZE; j++) {
                System.out.print(Utils.tlData.get().CELLS[i][j].getValue() + " ");
            }
            System.out.println();
        }
    }

    /**
     * 将当前9*9九宫格的所有单元格的实际情况转化为一个字符串
     */
    public static String getResultStr() {
        StringBuilder sbx = new StringBuilder();
        for (int i = 0; i < Utils.tlData.get().SIZE; i++) {
            for (int j = 0; j < Utils.tlData.get().SIZE; j++) {
                if (Utils.tlData.get().CELLS[i][j].getValue() != 0) {
                    sbx.append(Utils.tlData.get().CELLS[i][j].getValue());
                    appendBlack(sbx, 12);
                } else {
                    int a = 1;
                    StringBuilder sb = new StringBuilder();
                    final Object[] pValues = Utils.tlData.get().CELLS[i][j].getPValues();
                    sb.append("[");
                    for (int k = 0; k < pValues.length; k++) {
                        sb.append(pValues[k]);
                        a++;
                    }
                    sb.append("]");
                    sbx.append(sb.toString());
                    appendBlack(sbx, 12 - a);
                }
            }
            sbx.append("\n");
        }

        return sbx.toString();
    }

    /**
     * 打印当前9*9九宫格的所有单元格的实际情况
     * 如有未完成的单元格，会将其对应的所有候选值打印出来
     */
    public static void print() {
        if (Utils.tlData.get().TODOS.size() == 0) {
            printResult();
            return;
        }

        for (int i = 0; i < Utils.tlData.get().SIZE; i++) {
            for (int j = 0; j < Utils.tlData.get().SIZE; j++) {
                if (Utils.tlData.get().CELLS[i][j].getValue() != 0) {
                    System.out.print(Utils.tlData.get().CELLS[i][j].getValue());
                    printBlack(12);
                } else {
                    int a = 1;
                    StringBuilder sb = new StringBuilder();
                    final Object[] pValues = Utils.tlData.get().CELLS[i][j].getPValues();
                    sb.append("[");
                    for (int k = 0; k < pValues.length; k++) {
                        sb.append(pValues[k]);
                        a++;
                    }
                    sb.append("]");
                    System.out.print(sb.toString());
                    printBlack(12 - a);
                }
            }
            System.out.println();
        }
    }

    /**
     * 将object数组转化为int数组
     *
     * @param objs
     * @return
     */
    public static int[] objectToInt(Object[] objs) {
        int[] result = new int[objs.length];
        for (int i = 0; i < objs.length; i++) {
            result[i] = (int) objs[i];
        }

        return result;
    }

    /**
     * 将object数组转化为Cell数组
     *
     * @param objs
     * @return
     */
    public static Cell[] objectToCell(Object[] objs) {
        Cell[] result = new Cell[objs.length];
        for (int i = 0; i < objs.length; i++) {
            result[i] = (Cell) objs[i];
        }

        return result;
    }

    /**
     * 将object数组转化为 int ArrayList
     *
     * @param objs
     * @return
     */
    public static ArrayList<Integer> objectToArrayList(Object[] objs) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < objs.length; i++) {
            result.add((int) objs[i]);
        }

        return result;
    }

    /**
     * 将int数组转化为 int ArrayList
     *
     * @param ints
     * @return
     */
    public static ArrayList<Integer> intsToArrayList(int[] ints) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < ints.length; i++) {
            result.add(ints[i]);
        }

        return result;
    }

    /**
     * 只保存cells数组中的单元格中的nums候选值，这些单元格中其他的候选值删掉
     *
     * @param cells 需要处理的单元格数组
     * @param nums  需要保存的候选数字
     */
    public static boolean holdSpecifyCellsPV(Cell[] cells, int[] nums) {
        boolean result = false;

        for (int i = 0; i < cells.length; i++) {
            final boolean b = cells[i].holdSpecifyPV(nums);
            if (b) {
                result = true;
            }
        }

        return result;
    }

    /**
     * 将cell按候选值进行分类
     *
     * @param hsCells 需要分类的cell
     * @param map     分好类的结果
     */
    public static void getPV_map(HashSet<Cell> hsCells, Map<Integer, ArrayList<Cell>> map) {
        final Iterator<Cell> iterator = hsCells.iterator();
        while (iterator.hasNext()) {
            final Cell next = iterator.next();

            if (next.getValue() != 0) {
                continue;
            }

            final Object[] pValues = next.getPValues();
            for (int j = 0; j < pValues.length; j++) {
                int key = (int) pValues[j];
                ArrayList<Cell> cells = map.get(key);
                if (cells == null) {
                    cells = new ArrayList<>();
                    map.put(key, cells);
                }

                cells.add(next);
            }
        }
    }

    /**
     * 从按候选数字好类的map对象中查找候选数字出现频率在2-limit之间的候选数字
     *
     * @param map
     * @param from  开始查找的位置(是下标，从零开始的)
     * @param limit
     * @return 返回符合条件的第一个候选数字
     */
    public static int getCandidateKey(Map<Integer, ArrayList<Cell>> map, int from, int limit) {
        final Data data = Utils.tlData.get();
        for (int i = from; i < data.SIZE; i++) {
            final ArrayList<Cell> cells = map.get(i + 1);

            if (cells == null) {
                continue;
            }

            if (cells.size() <= limit && cells.size() >= 2) {
                return i + 1;
            }
        }

        return -1;
    }

    /**
     * 获取cellsAls所包含的所有行/列组成的集合
     *
     * @param type     1：表示需要获取cellsAls所包含的所有列，2：表示需要获取cellsAls所包含的所有行
     * @param cellsAls
     * @return
     */
    public static HashSet<Integer> getUnique(int type, ArrayList<Cell>... cellsAls) {
        HashSet<Integer> hsIn = new HashSet<>();

        for (int i = 0; i < cellsAls.length; i++) {
            final ArrayList<Cell> cellsAl = cellsAls[i];
            final Iterator<Cell> iterator = cellsAl.iterator();
            while (iterator.hasNext()) {
                final Cell next = iterator.next();
                if (type == 1) {
                    hsIn.add(next.getY());
                } else if (type == 2) {
                    hsIn.add(next.getX());
                }
            }
        }

        return hsIn;
    }

    /**
     * 步骤开头的字符串
     *
     * @return
     */
    public static String getStepHeader() {
        return "第" + (step++) + "步、";
    }

    // 打印空格，为了print方法的左对齐输出！！！
    private static void printBlack(int num) {
        for (int i = 0; i < num; i++) {
            System.out.print(" ");
        }
    }

    // 为StringBuilder对象append空格
    private static void appendBlack(StringBuilder sb, int num) {
        for (int i = 0; i < num; i++) {
            sb.append(" ");
        }
    }

    /**
     * 判断expects是否包含cell
     *
     * @param cell
     * @param expects
     * @return true：包含
     */
    private static boolean isIncludeCell(Cell cell, Cell[] expects) {
        for (int i = 0; i < expects.length; i++) {
            if (cell.equals(expects[i])) {
                return true;
            }
        }

        return false;
    }
}
