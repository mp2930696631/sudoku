package data;

import core.Config;
import core.Utils;
import core.entity.Cell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author zehua
 * @date 2021/3/30 8:27
 * <p>
 * 存放数独相关的重要数据的地方，如果想添加算法，唯一需要获取的对象就是该对象，获取方式Utils.tlData.get()
 * 注：其实并不需要将data实例对象放置在Utils中的ThreadLocal对象中！！（因为这是单线程环境）
 */
public class Data implements Serializable {

    public final int SIZE = 9;
    // 保存原始数独数据
    public final Cell[][] CELLS = new Cell[SIZE][SIZE];
    // 保存数独中的待填的空格
    public final ArrayList<Cell> TODOS = new ArrayList<>();
    // 保存以填好的空格对象
    public final ArrayList<Cell>[] DONE_CELLS = new ArrayList[SIZE];

    // 数独中按行下标保存每一行中待填的单元格对象
    public final HashSet<Cell>[] ROW_LAST_CELLS = new HashSet[SIZE];
    // 数独中按列下标保存每一列中待填的单元格对象
    public final HashSet<Cell>[] COLUMN_LAST_CELLS = new HashSet[SIZE];
    // 数独中按小九宫格下标保存每一小九格中待填的单元格对象
    public final HashSet<Cell>[] LITTLE_SUDOKU_LAST_CELLS = new HashSet[SIZE];

    /**
     * 从磁盘加载数独数据并初始化data对象中的相关数据属性！！
     */
    public boolean loadAndInitData() throws Exception {
        for (int i = 0; i < SIZE; i++) {
            DONE_CELLS[i] = new ArrayList();
            ROW_LAST_CELLS[i] = new HashSet<>();
            COLUMN_LAST_CELLS[i] = new HashSet<>();
            LITTLE_SUDOKU_LAST_CELLS[i] = new HashSet<>();
        }

        boolean result = true;
        result = loadData();
        init();
        return result;
    }

    // 从磁盘中加载数独数据
    private boolean loadData() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(Config.data_path))) {
            String line = br.readLine();
            int n = 0;
            while (line != null) {
                if (line.length() > SIZE) {
                    return false;
                }

                for (int i = 0; i < SIZE; i++) {
                    Cell cell = new Cell(n, i, 0);
                    if (line.charAt(i) == '_') {
                        cell.setValue(0);
                        TODOS.add(cell);
                    } else {
                        cell.setValue(line.charAt(i) - 48);
                    }
                    CELLS[n][i] = cell;
                }
                n++;
                line = br.readLine();
            }

            return true;
        } catch (Exception e) {
            throw e;
        }

    }

    // 初始化数据DONE_CELLS、ROW_LAST_CELLS、COLUMN_LAST_CELLS、LITTLE_SUDOKU_LAST_CELLS
    private void init() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                final Cell cell = CELLS[i][j];
                if (cell.getValue() == 0) {
                    calculatePV(cell);
                    ROW_LAST_CELLS[i].add(cell);
                    COLUMN_LAST_CELLS[j].add(cell);
                    int startX = i / 3;
                    int startY = j / 3;
                    LITTLE_SUDOKU_LAST_CELLS[startY * 3 + startX].add(cell);
                } else {
                    DONE_CELLS[cell.getValue() - 1].add(cell);
                }
            }
        }
    }

    /**
     * 计算对应的单元格的所有候选值，只会在从磁盘加载数据的时候计算一次，以后再程序运行的过程中都不会再执行
     * 因为程序在执行过程中，只会动态的减少候选值，并不会重新计算候选值
     *
     * @param cell 需要计算候选值的单元格
     */
    private void calculatePV(Cell cell) {
        final int x = cell.getX();
        final int y = cell.getY();

        final HashSet<Integer> nums = getNums(x, y);

        for (int i = 1; i <= 9; i++) {
            if (!nums.contains(i)) {
                cell.addPValue(i);
            }
        }
    }

    /**
     * 获取对应单元格所在行、列、小九宫格所有的不重复的数字集合
     *
     * @param x
     * @param y
     * @return
     */
    private HashSet<Integer> getNums(int x, int y) {
        HashSet<Integer> result = new HashSet<>();
        countCeil(x, y, result);
        countXY(x, y, result);

        return result;
    }

    /**
     * 获取单元格所在小九宫格中所有不重复的数字的集合
     *
     * @param x
     * @param y
     * @param result
     */
    private void countCeil(int x, int y, HashSet<Integer> result) {
        int startX = x / 3;
        int startY = y / 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result.add(Utils.tlData.get().CELLS[startX * 3 + i][startY * 3 + j].getValue());
            }
        }
    }

    /**
     * 获取单元格所在行、列中所有不重复的数字的集合
     *
     * @param x
     * @param y
     * @param result
     */
    private void countXY(int x, int y, HashSet<Integer> result) {
        for (int i = 0; i < Utils.tlData.get().SIZE; i++) {
            result.add(Utils.tlData.get().CELLS[x][i].getValue());
            result.add(Utils.tlData.get().CELLS[i][y].getValue());
        }
    }
}
