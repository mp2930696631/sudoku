package arithmetic.lockedCandidates;

import arithmetic.ArithmeticInterf;
import core.Config;
import core.Constants;
import core.Utils;
import core.entity.Cell;
import data.Data;

import java.util.*;

/**
 * 区块摒除法
 *
 * @author zehua
 * @date 2021/3/30 7:40
 */
public class LC implements ArithmeticInterf {
    @Override
    public boolean run() {
        final Data data = Utils.tlData.get();
        doRun(data.ROW_LAST_CELLS, 1);
        doRun(data.COLUMN_LAST_CELLS, 2);
        doRun(data.LITTLE_SUDOKU_LAST_CELLS, 3);

        return true;
    }

    /**
     * @param hsCells
     */
    private void doRun(HashSet<Cell>[] hsCells, int type) {
        final Data data = Utils.tlData.get();
        for (int i = 0; i < data.SIZE; i++) {
            Map<Integer, ArrayList<Cell>> map = new HashMap();
            final HashSet<Cell> hsCell = hsCells[i];

            Utils.getPV_map(hsCell, map);

            findPV_block(map, type);
        }
    }

    /**
     * 一个1*3的区块上面仅包含的相同数字的个数, 取值为2、3
     *
     * @param map
     * @param type 注意，这个方法的type与doFindPV_block方法中type参数是不同的
     *             1：表示在行中寻找区块
     *             2：表示在列中寻找区块
     *             3：表示在小九宫格中寻找区块
     * @return
     */
    private void findPV_block(Map<Integer, ArrayList<Cell>> map, int type) {
        Data data = Utils.tlData.get();

        for (int i = 0; i < data.SIZE; i++) {
            final ArrayList<Cell> cells = map.get(i + 1);

            if (cells == null) {
                continue;
            }

            if (cells.size() <= 3 && cells.size() >= 2) {
                int[] nums = new int[]{i + 1};
                switch (type) {
                    case 1:
                    case 2:
                        doFindPV_block(cells, 3, nums);
                        break;
                    case 3:
                        doFindPV_block(cells, 1, nums);
                        doFindPV_block(cells, 2, nums);
                        break;
                    default:
                        System.out.println("...type error..");
                        break;
                }
            }

        }
    }

    /**
     * @param cells
     * @param type  1表示行，也就是小九宫格中有对应候选者的区块，并且区块中的单元格位于同一行，这样就可以将该行其他单元格对应的候选数字删除了
     *              2表示列, 同上
     *              3表示小九宫格，也就是在行/列中存在区块，并且，该区块中的单元格也位于同一个小九宫格，这样就可以将该小九宫格其他单元格对应的候选数字删除了
     * @param nums  表示需要排除的数字组成的数组
     */
    private void doFindPV_block(ArrayList<Cell> cells, int type, int[] nums) {
        final Cell cell = cells.get(0);
        boolean flag = true;
        Iterator<Cell> iterator = cells.iterator();

        boolean b = false;
        String blockDirection = "";
        String direction = "";

        switch (type) {
            case 1:
                while (iterator.hasNext()) {
                    final Cell next = iterator.next();
                    // 判断是否位于同行
                    flag = flag && (cell.getX() == next.getX());
                }

                // 表示存在！可以删除该行其他单元格相应的候选数字了
                if (flag) {
                    b = Utils.removePV_row(nums, cell, Utils.objectToCell(cells.toArray()));
                    blockDirection = Constants.DIRECTION_3;
                    direction = Constants.DIRECTION_1;
                }
                break;
            case 2:
                while (iterator.hasNext()) {
                    final Cell next = iterator.next();
                    // 判断是否位于同列
                    flag = flag && (cell.getY() == next.getY());
                }
                // 表示存在！可以删除该列其他单元格相应的候选数字了
                if (flag) {
                    b = Utils.removePV_column(nums, cell, Utils.objectToCell(cells.toArray()));
                    blockDirection = Constants.DIRECTION_3;
                    direction = Constants.DIRECTION_2;
                }
                break;
            case 3:
                while (iterator.hasNext()) {
                    final Cell next = iterator.next();
                    // 判断是否位于同一个小九宫格
                    flag = flag && (cell.getY() / 3 == next.getY() / 3 && cell.getX() / 3 == next.getX() / 3);
                }

                // 表示存在！可以删除该小九宫格其他单元格相应的候选数字了
                if (flag) {
                    b = Utils.removePV_littleSudoku(nums, cell, Utils.objectToCell(cells.toArray()));
                    blockDirection = Constants.DIRECTION_1 + "/" + Constants.DIRECTION_2;
                    direction = Constants.DIRECTION_3;
                }
                break;
            default:
                System.out.println("type error....");
                break;
        }

        if (b) {
            printStep(cells, nums[0], Constants.LC_NAME, blockDirection, direction);
        }
    }

    private void printStep(ArrayList<Cell> cells, int pv, String arithmeticName, String blockDirection, String direction) {
        if (Config.print_step) {
            StringBuilder sb = new StringBuilder();
            sb.append(Utils.getStepHeader());
            sb.append("相关单元格：");
            sb.append(cells);
            sb.append(", ");
            sb.append("候选数：");
            sb.append(pv);
            sb.append(", ");
            sb.append("区块：" + blockDirection);
            sb.append(", ");
            sb.append("方向：" + direction);
            sb.append(", ");
            sb.append("(" + arithmeticName + ")");
            sb.append("\n");

            Utils.write(sb.toString());

            if (Config.print_sudoku_detail) {
                Utils.write(Utils.getResultStr());
            }
        }
    }
}
