package arithmetic.recessiveUniqueCandidate;

import arithmetic.ArithmeticInterf;
import core.Config;
import core.Constants;
import core.Utils;
import core.entity.Cell;

import java.util.*;

/**
 * 隐式唯一候选者算法
 *
 * @author zehua
 * @date 2021/3/30 7:41
 */
public class RUCM implements ArithmeticInterf {
    @Override
    public boolean run() {
        doRun(Utils.tlData.get().ROW_LAST_CELLS);
        doRun(Utils.tlData.get().COLUMN_LAST_CELLS);
        doRun(Utils.tlData.get().LITTLE_SUDOKU_LAST_CELLS);

        return true;
    }

    private void doRun(HashSet<Cell>[] hses) {
        for (int i = 0; i < Utils.tlData.get().SIZE; i++) {
            Map<Integer, ArrayList<Cell>> map = new HashMap<>();
            final HashSet<Cell> hsCells = hses[i];
            Utils.getPV_map(hsCells, map);

            for (int j = 0; j < Utils.tlData.get().SIZE; j++) {
                // 如果有隐性唯一候选数法
                final ArrayList<Cell> cells = map.get(j + 1);
                if (cells != null && cells.size() == 1) {
                    final Cell cell = cells.get(0);
                    cell.setValue(j + 1);
                    Utils.tlData.get().TODOS.remove(cell);

                    Utils.removePV(new int[]{j + 1}, cell, null);

                    printStep(cell, cell.getValue(), Constants.RUCM_NAME);
                }
            }
        }
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

            if (Config.print_sudoku_detail) {
                Utils.write(Utils.getResultStr());
            }
        }
    }
}
