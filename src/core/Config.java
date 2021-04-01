package core;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * @author zehua
 * @date 2021/4/1 8:27
 */
public class Config {
    // 配置文件的配置信息的默认value
    public static String data_path = "F:\\zehua\\pictures\\数独\\数据\\17\\data.txt";
    public static String recursion_dir = "F:\\zehua\\pictures\\数独\\数据\\recursion\\";
    public static boolean using_recursion_try = true;
    public static boolean print_step = false;
    public static boolean print_sudoku_detail = false;
    public static String step_sudoku_detail_file_path = "F:\\zehua\\pictures\\数独\\step\\resolve.txt";

    static {
        loadConfig();
    }

    private static void loadConfig() {
        File config = new File(Constants.config_file_path);
        if (config.exists()) {
            try (FileReader fr = new FileReader(config)) {
                Properties p = new Properties();
                p.load(fr);

                if (p.get(Constants.data_path_key) != null) {
                    data_path = (String) p.get(Constants.data_path_key);
                }

                if (p.get(Constants.recursion_dir_key) != null) {
                    recursion_dir = (String) p.get(Constants.recursion_dir_key);
                }

                if (p.get(Constants.using_recursion_try_key) != null) {
                    using_recursion_try = Boolean.parseBoolean((String) p.get(Constants.using_recursion_try_key));
                }

                if (p.get(Constants.print_step_key) != null) {
                    print_step = Boolean.parseBoolean((String) p.get(Constants.print_step_key));
                }

                if (p.get(Constants.print_sudoku_detail_key) != null) {
                    print_sudoku_detail = Boolean.parseBoolean((String) p.get(Constants.print_sudoku_detail_key));
                }

                if (p.get(Constants.step_sudoku_detail_file_path_key) != null) {
                    step_sudoku_detail_file_path = (String) p.get(Constants.step_sudoku_detail_file_path_key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
