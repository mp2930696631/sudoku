import arithmetic.base.BaseArithmetic;
import arithmetic.candidatePairSubtraction.ExplicitCPS;
import arithmetic.candidatePairSubtraction.ImplicitCPS;
import arithmetic.lockedCandidates.LC;
import arithmetic.recessiveUniqueCandidate.RUCM;
import arithmetic.swordfish.Swordfish;
import arithmetic.threeChainDeletion.ExplicitTCD;
import arithmetic.threeChainDeletion.ImplicitTCD;
import arithmetic.xWing.XWing;
import core.ArithmeticExecutor;
import core.Utils;
import data.Data;

/**
 * @author zehua
 * @date 2021/3/30 7:33
 */
public class SudokuMain {

    public static void main(String[] args) {
        initArithmetics();

        Data data = new Data();
        Utils.tlData.set(data);
        // 为了执行Data类中的static代码块
        boolean b = true;
        try {
            b = data.loadAndInitData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!b) {
            System.out.println("data error....");
            System.out.println("data: ");
            Utils.printResult();
            return;
        }

        // 开始执行
        ArithmeticExecutor.run();

        // 将步骤输出到文件
        Utils.flush();

        // 打印结果
        Utils.print();
    }

    private static void initArithmetics() {
        BaseArithmetic baseArithmetic = new BaseArithmetic();
        ExplicitCPS explicitCPS = new ExplicitCPS();
        ImplicitCPS implicitCPS = new ImplicitCPS();
        LC lc = new LC();
        RUCM rucm = new RUCM();
        Swordfish swordfish = new Swordfish();
        ExplicitTCD explicitTCD = new ExplicitTCD();
        ImplicitTCD implicitTCD = new ImplicitTCD();
        XWing xWing = new XWing();

        ArithmeticExecutor.addUniqueArithmetic(baseArithmetic);
        ArithmeticExecutor.addUniqueArithmetic(rucm);

        ArithmeticExecutor.addExclusionArithmetic(explicitCPS);
        ArithmeticExecutor.addExclusionArithmetic(implicitCPS);
        ArithmeticExecutor.addExclusionArithmetic(lc);
        ArithmeticExecutor.addExclusionArithmetic(swordfish);
        ArithmeticExecutor.addExclusionArithmetic(explicitTCD);
        ArithmeticExecutor.addExclusionArithmetic(implicitTCD);
        ArithmeticExecutor.addExclusionArithmetic(xWing);
    }


}
