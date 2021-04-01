package arithmetic.candidatePairSubtraction;

import arithmetic.ArithmeticInterf;
import core.Config;
import core.Constants;
import core.Utils;
import core.entity.Cell;

import java.util.*;

/**
 * 候选数对删减法(显式)
 *
 * @author zehua
 * @date 2021/3/30 7:39
 */
public class ExplicitCPS implements ArithmeticInterf {
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
            for (int j = 0; j < size - 1; j++) {
                final Cell cell1 = (Cell) objects[j];

                // 必须是未填空格才执行
                if (cell1.getValue() != 0 || !pvLengthCondition(cell1, 2)) {
                    continue;
                }
                for (int k = j + 1; k < size; k++) {
                    final Cell cell2 = (Cell) objects[k];

                    // 必须是未填空格才执行
                    if (cell2.getValue() != 0 || !pvLengthCondition(cell2, 2)) {
                        continue;
                    }
                    final boolean pairEqual = cell1.isPairEqual(cell2);
                    // 找到了候选数对
                    if (pairEqual) {
                        final Object[] pValues = cell1.getPValues();
                        final int[] ints = Utils.objectToInt(pValues);
                        final boolean b = Utils.removePV_byType(ints, cell1, new Cell[]{cell1, cell2}, type);

                        if (b) {
                            ArrayList<Cell> relatedCells = new ArrayList<>();
                            relatedCells.add(cell1);
                            relatedCells.add(cell2);
                            printStep(relatedCells, Utils.intsToArrayList(ints), Constants.EXPLICIT_CPS_NAME, type);
                        }
                    }
                }
            }
        }
    }

    private boolean pvLengthCondition(Cell cell, int limit) {
        return cell.getHashSetPValues().size() >= 2 && cell.getHashSetPValues().size() <= limit;
    }

    private void printStep(ArrayList<Cell> cells, ArrayList<Integer> pvs, String arithmeticName, int type) {
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
