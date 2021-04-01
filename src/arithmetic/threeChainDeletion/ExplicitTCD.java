package arithmetic.threeChainDeletion;

import arithmetic.ArithmeticInterf;
import core.Config;
import core.Constants;
import core.Utils;
import core.entity.Cell;

import java.util.*;

/**
 * 三链数删减法(显式)
 *
 * @author zehua
 * @date 2021/3/30 7:43
 */
public class ExplicitTCD implements ArithmeticInterf {
    @Override
    public boolean run() {
        doRun(Utils.tlData.get().ROW_LAST_CELLS, 1);
        doRun(Utils.tlData.get().COLUMN_LAST_CELLS, 2);
        doRun(Utils.tlData.get().LITTLE_SUDOKU_LAST_CELLS, 3);

        return true;
    }

    private void doRun(HashSet<Cell>[] hses, int type) {
        for (int i = 0; i < Utils.tlData.get().SIZE; i++) {
            final HashSet<Cell> hs = hses[i];
            final int size = hs.size();
            final Object[] objects = hs.toArray();
            for (int j = 0; j < size - 2; j++) {
                final Cell cell1 = (Cell) objects[j];

                // 必须是未填空格才执行
                if (cell1.getValue() != 0 || !pvLengthCondition(cell1, 3)) {
                    continue;
                }

                for (int k = j + 1; k < size - 1; k++) {
                    final Cell cell2 = (Cell) objects[k];

                    // 必须是未填空格才执行
                    if (cell2.getValue() != 0 || !pvLengthCondition(cell2, 3)) {
                        continue;
                    }

                    for (int x = k + 1; x < size; x++) {
                        final Cell cell3 = (Cell) objects[x];

                        // 必须是未填空格才执行
                        if (cell3.getValue() != 0 || !pvLengthCondition(cell1, 3)) {
                            continue;
                        }

                        HashSet<Integer> result = new HashSet<>();
                        final boolean threeChainNums = isThreeChainNums(cell1, cell2, cell3, result);
                        // 找到了三链数
                        if (threeChainNums) {
                            final Object[] objects1 = result.toArray();
                            final int[] ints = Utils.objectToInt(objects1);
                            final boolean b = Utils.removePV_byType(ints, cell1, new Cell[]{cell1, cell2, cell3}, type);
                            if (b) {
                                ArrayList<Cell> cells = new ArrayList<>();
                                cells.add(cell1);
                                cells.add(cell2);
                                cells.add(cell3);
                                printStep(cells, result, Constants.EXPLICIT_TCD_NAME, type);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isThreeChainNums(Cell a, Cell b, Cell c, HashSet<Integer> result) {
        final HashSet<Integer> aHashSetPValues = a.getHashSetPValues();
        final HashSet<Integer> bHashSetPValues = b.getHashSetPValues();
        final HashSet<Integer> cHashSetPValues = c.getHashSetPValues();
        if (pvLengthCondition(a, 3) && pvLengthCondition(b, 3) && pvLengthCondition(c, 3)) {
            result.addAll(aHashSetPValues);
            result.addAll(bHashSetPValues);
            result.addAll(cHashSetPValues);

            return result.size() == 3;
        }

        return false;
    }

    private boolean pvLengthCondition(Cell cell, int limit) {
        return cell.getHashSetPValues().size() >= 2 && cell.getHashSetPValues().size() <= limit;
    }

    private void printStep(ArrayList<Cell> cells, HashSet<Integer> pvs, String arithmeticName, int type) {
        if (Config.print_step) {
            StringBuilder sb = new StringBuilder();
            sb.append(Utils.getStepHeader());
            sb.append("相关单元格：");
            sb.append(cells);
            sb.append(", ");
            sb.append("候选数：");
            sb.append(pvs);
            sb.append(", ");
            sb.append("方向：");
            switch (type) {
                case 1:
                    sb.append(Constants.DIRECTION_1);
                    break;
                case 2:
                    sb.append(Constants.DIRECTION_2);
                    break;
                case 3:
                    sb.append(Constants.DIRECTION_3);
                    break;
            }
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
