package core.entity;

import core.Probe;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author zehua
 * @date 2021/3/30 7:34
 */
public class Cell implements Comparable<Cell>, Serializable {
    /**
     * x: 该宫位于的行下标（从零开始）
     * y：该宫位于的列下标（从零开始）
     * value：该宫的正确的值
     * possibleValue：该宫所有的候选值
     * <p>
     * 注意：如果需要判断该宫是否已经被确认，需要使用value!=0表示该单元格已经被确认
     * 而不能使用possibleValue是否为空来判断，因为程序在执行的过程中，可能并不会即时将已经排除的候选值从possibleValue中移除
     */
    private int x, y, value;
    private HashSet<Integer> possibleValue = new HashSet<>();

    public Cell() {

    }

    public Cell(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    // 添加候选值
    public void addPValue(int pValue) {
        possibleValue.add(pValue);
    }

    // 移除候选值
    public void removePValue(int pValue) {
        final boolean remove = possibleValue.remove(pValue);

        // 这里使用到了Probe中的FLAG变量
        // 也就是如果有候选值发生了变化，就表示某算法对数独的求解做出了贡献，就暂时不需要使用递归了
        if (remove) {
            Probe.setFlag(true);
        }
    }

    // 批量移除候选值
    public boolean removePValues(int[] pValues) {
        boolean result = false;
        for (int i = 0; i < pValues.length; i++) {
            final boolean remove = possibleValue.remove(pValues[i]);

            // 这里使用到了Probe中的FLAG变量
            // 也就是如果有候选值发生了变化，就表示某算法对数独的求解做出了贡献，就暂时不需要使用递归了
            if (remove) {
                Probe.setFlag(true);
                result = true;
            }
        }

        return result;
    }

    // 批量移除候选值
    // 虽然传入的是Object，但实际上是integer
    public void removePValues(Object[] pValues) {
        for (int i = 0; i < pValues.length; i++) {
            final boolean remove = possibleValue.remove(pValues[i]);

            // 这里使用到了Probe中的FLAG变量
            // 也就是如果有候选值发生了变化，就表示某算法对数独的求解做出了贡献，就暂时不需要使用递归了
            if (remove) {
                Probe.setFlag(true);
            }
        }
    }

    /**
     * 移除nums外其他的候选值
     * <p>
     * 一些算法用得到，比如隐式的三链数删减法
     *
     * @param nums 需要保留的候选值
     */
    public boolean holdSpecifyPV(int[] nums) {
        boolean result = false;

        for (int i = 0; i < nums.length; i++) {
            if (possibleValue.contains(nums[i])) {
                continue;
            }

            final boolean remove = possibleValue.remove(nums[i]);
            // 这里使用到了Probe中的FLAG变量
            // 也就是如果有候选值发生了变化，就表示某算法对数独的求解做出了贡献，就暂时不需要使用递归了
            if (remove) {
                Probe.setFlag(true);
                result = true;
            }
        }

        return result;
    }

    /**
     * @return 如果候选值唯一，返回true
     */
    public boolean isInsure() {
        return possibleValue.size() == 1;
    }

    /*
    根据下标获取候选值，
    因为possibleValue是一个HashSet对象，使用下标来获取候选值基本没有意义，除非index=0
    所以，该方法的入参基本固定为0！！！
     */
    public int getPValue(int index) {
        return (int) possibleValue.toArray()[index];
    }

    // 获取所有的候选值
    public Object[] getPValues() {
        return possibleValue.toArray();
    }

    // 获取possibleValue对象
    public HashSet<Integer> getHashSetPValues() {
        return possibleValue;
    }

    // 清除所有的候选值
    public void clear() {
        possibleValue.clear();
    }

    // 判断两个单元格的候选值是否完全相等，且只有两个候选值
    public boolean isPairEqual(Cell o) {
        if (this.possibleValue.size() == 2 && o.possibleValue.size() == 2) {
            final Iterator<Integer> iterator = o.possibleValue.iterator();
            while (iterator.hasNext()) {
                if (!this.possibleValue.contains(iterator.next())) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    // 排序用得到
    public int compareTo(Cell o) {
        return this.possibleValue.size() - o.possibleValue.size();
    }

    @Override
    public String toString() {
        return "(" + (x + 1) + "," + (y + 1) + ")";
    }
}
