package core;

/**
 * @author zehua
 * @date 2021/3/30 7:34
 */
public class Constants {
    public static String config_file_path = "C:\\sudoku\\config.properties";

    // 配置文件的配置信息的key
    public static String data_path_key = "data.path";
    public static String recursion_dir_key = "recursion.dir";
    public static String using_recursion_try_key = "using.recursion.try";
    public static String print_step_key = "print.step";
    public static String print_sudoku_detail_key = "print.sudoku.detail";
    public static String step_sudoku_detail_file_path_key = "step.sudoku.detail.file.path";

    // 算法名
    public static final String BASE_ARITHMETIC_NAME = "显式唯一候选者算法";
    public static final String RUCM_NAME = "隐式唯一候选者算法";
    public static final String EXPLICIT_CPS_NAME = "候选数对删减法(显式)";
    public static final String IMPLICIT_CPS_NAME = "候选数对删减法(隐式)";
    public static final String LC_NAME = "区块摒除法";
    public static final String RECURSION_TRY_NAME = "递归试错法";
    public static final String SWORDFISH_NAME = "三链列删减法";
    public static final String EXPLICIT_TCD_NAME = "三链数删减法(显式)";
    public static final String IMPLICIT_TCD_NAME = "三链数删减法(隐式)";
    public static final String XWING_NAME = "矩形删除法";

    // 区块摒除法详细描述
    /*public static final String DETAIL_1 = "区块在行，摒除小九宫格中数据";
    public static final String DETAIL_2 = "区块在列，摒除小九宫格中数据";
    public static final String DETAIL_3 = "区块在小九宫格，摒除行中数据";
    public static final String DETAIL_4 = "区块在小九宫格，摒除列中数据";*/

    public static final String DIRECTION_1 = "行";
    public static final String DIRECTION_2 = "列";
    public static final String DIRECTION_3 = "小九宫格";

    /*public static void main(String[] args) {
        ArrayList<Cell> cells = new ArrayList<>();
        ArrayList<Integer> ins = new ArrayList<>();
        cells.add(new Cell(0,1,2));
        cells.add(new Cell(3,6,9));
        ins.add(1);
        ins.add(2);

        System.out.println(cells);
        System.out.println(ins);
    }*/

    /*public static void main(String[] args) {
        final InputStream resourceAsStream = Constants.class.getResourceAsStream("config.properties");
        try (InputStreamReader isr= new InputStreamReader(resourceAsStream)){
            Properties prop = new Properties();
            prop.load(isr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
}
