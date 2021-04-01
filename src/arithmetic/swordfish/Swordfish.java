package arithmetic.swordfish;

import arithmetic.ArithmeticInterf;
import core.Config;
import core.Constants;
import core.Utils;
import core.entity.Cell;
import data.Data;

import java.util.*;

/**
 * 剑鱼算法（三链列删减法）
 *
 * @author zehua
 * @date 2021/3/30 7:42
 */
public class Swordfish implements ArithmeticInterf {
    @Override
    public boolean run() {
        final Data data = Utils.tlData.get();
        Map<Integer, ArrayList<Cell>>[] mapsR = new HashMap[data.SIZE];
        Map<Integer, ArrayList<Cell>>[] mapsC = new HashMap[data.SIZE];

        for (int i = 0; i < data.SIZE; i++) {
            mapsR[i] = new HashMap<>();
            mapsC[i] = new HashMap<>();
        }

        // 行
        for (int i = 0; i < data.SIZE; i++) {
            Utils.getPV_map(data.ROW_LAST_CELLS[i], mapsR[i]);
        }

        // 列
        for (int i = 0; i < data.SIZE; i++) {
            Utils.getPV_map(data.COLUMN_LAST_CELLS[i], mapsC[i]);
        }

        doRun(mapsR, 1);
        doRun(mapsC, 2);

        return true;
    }

    /**
     * 在行列方向上找只出现在两行的数字
     *
     * @param maps
     * @param type 1表示行、2表示列
     */
    private void doRun(Map<Integer, ArrayList<Cell>>[] maps, int type) {
        final Data data = Utils.tlData.get();

        for (int i = 0; i < data.SIZE - 2; i++) {
            Map<Integer, ArrayList<Cell>> map = maps[i];

            outer:
            for (int j = 0; j < data.SIZE; ) {
                j = Utils.getCandidateKey(map, j, 3);

                if (j == -1) {
                    break;
                }

                for (int k = i + 1; k < data.SIZE - 1; k++) {
                    Map<Integer, ArrayList<Cell>> mapx = maps[k];

                    if (mapx.get(j) != null && mapx.get(j).size() <= 3 && mapx.get(j).size() >= 2) {
                        // 继续往下找
                        for (int x = k + 1; x < data.SIZE; x++) {
                            Map<Integer, ArrayList<Cell>> mapxx = maps[x];

                            if (mapxx.get(j) != null && mapxx.get(j).size() <= 3 && mapxx.get(j).size() >= 2) {
                                final ArrayList<Cell> cells = map.get(j);
                                final ArrayList<Cell> cells1 = mapx.get(j);
                                final ArrayList<Cell> cells2 = mapxx.get(j);

                                final HashSet<Integer> unique = Utils.getUnique(type, cells, cells1, cells2);
                                if (unique.size() != 3) {
                                    continue;
                                }

                                HashSet<Cell> allCells = new HashSet<>();
                                allCells.addAll(cells);
                                allCells.addAll(cells1);
                                allCells.addAll(cells2);
                                final Cell[] objectToCell = Utils.objectToCell(allCells.toArray());

                                boolean b = false;
                                String direction = "";

                                if (type == 1) {
                                    direction = Constants.DIRECTION_1;
                                    for (int n = 0; n < objectToCell.length; n++) {
                                        final Cell cell = objectToCell[n];
                                        if (unique.remove(cell.getY())) {
                                            final boolean b1 = Utils.removePV_column(new int[]{j}, cell, objectToCell);
                                            if (b1) {
                                                b = true;
                                            }
                                        }
                                    }
                                } else if (type == 2) {
                                    direction = Constants.DIRECTION_2;
                                    for (int n = 0; n < objectToCell.length; n++) {
                                        final Cell cell = objectToCell[n];
                                        if (unique.remove(cell.getX())) {
                                            final boolean b1 = Utils.removePV_row(new int[]{j}, cell, objectToCell);
                                            if (b1) {
                                                b = true;
                                            }
                                        }
                                    }
                                }

                                if (b) {
                                    printStep(allCells, j, Constants.SWORDFISH_NAME, direction);
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private void printStep(HashSet<Cell> cells, Integer pv, String arithmeticName, String direction) {
        if (Config.print_step) {
            StringBuilder sb = new StringBuilder();
            sb.append(Utils.getStepHeader());
            sb.append("相关单元格：");
            sb.append(cells);
            sb.append(", ");
            sb.append("候选数：");
            sb.append(pv);
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
