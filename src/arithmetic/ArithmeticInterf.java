package arithmetic;

/**
 * @author zehua
 * @date 2021/3/30 7:36
 */
public interface ArithmeticInterf {
    default void beforeRun() {

    }

    /**
     *
     * @return
     */
    boolean run();

    default void afterRun() {

    }
}
