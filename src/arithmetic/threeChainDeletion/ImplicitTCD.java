package arithmetic.threeChainDeletion;

import arithmetic.ArithmeticInterf;
import core.Config;
import core.Constants;
import core.Utils;
import core.entity.Cell;
import data.Data;

import java.util.*;

/**
 * 三链数删减法(隐式)
 *
 * @author zehua
 * @date 2021/3/30 7:44
 */
public class ImplicitTCD implements ArithmeticInterf {
    @Override
    public boolean run() {
        doRun(Utils.tlData.get().ROW_LAST_CELLS, 1);
        doRun(Utils.tlData.get().COLUMN_LAST_CELLS, 2);
        doRun(Utils.tlData.get().LITTLE_SUDOKU_LAST_CELLS, 3);

        return true;
    }

    private void doRun(HashSet<Cell>[] hses, int type) {
        final Data data = Utils.tlData.get();
        for (int i = 0; i < data.SIZE; i++) {
            final HashSet<Cell> hs = hses[i];
            Map<Integer, ArrayList<Cell>> map = new HashMap<>();

            Utils.getPV_map(hs, map);

            outer:
            for (int j = 0; j < data.SIZE;) {
                j = Utils.getCandidateKey(map, j, 3);
                if (j == -1) {
                    break;
                }

                for (int k = j; k < data.SIZE;) {
                    k = Utils.getCandidateKey(map, k, 3);

                    // 可以进一步处理
                    if (k == -1) {
                        break;
                    }

                    for (int x = k; x < data.SIZE;) {
                        x = Utils.getCandidateKey(map, x, 3);

                        if (x == -1) {
                            break;
                        }


                        final ArrayList<Cell> cells1 = map.get(j);
                        final ArrayList<Cell> cells2 = map.get(k);
                        final ArrayList<Cell> cells3 = map.get(x);
                        HashSet<Cell> hsCells = new HashSet<>();
                        hsCells.addAll(cells1);
                        hsCells.addAll(cells2);
                        hsCells.addAll(cells3);

                        // 找到了
                        if (hsCells.size() == 3) {
                            int[] pvs = new int[]{j, k, x};
                            final boolean b = Utils.holdSpecifyCellsPV(Utils.objectToCell(hsCells.toArray()), pvs);
                            if (b){
                                printStep(hsCells, Utils.intsToArrayList(pvs), Constants.IMPLICIT_CPS_NAME);
                            }
                        }
                    }
                }

            }
        }
    }

    private void printStep(HashSet<Cell> cells, ArrayList<Integer> pvs, String arithmeticName) {
        if (Config.print_step) {
            StringBuilder sb = new StringBuilder();
            sb.append(Utils.getStepHeader());
            sb.append("相关单元格：");
            sb.append(cells);
            sb.append(", ");
            sb.append("候选数：");
            sb.append(pvs);
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
