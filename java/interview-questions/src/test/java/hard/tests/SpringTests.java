package hard.tests;

import org.junit.jupiter.api.Test;

public class SpringTests {
    @Test
    public void cyclicDependencyTest() {
        // reflection magic
    }

    @Test
    public void wrappersCombinationTests() {
        // Вариант 1
        // @Async // или @Audit
        // @Transactional
        // public void doSmth() {
        // }
        // @Async // а если в разных бинах?
        // public void doSmth2() {
        //    repo1.doSmth(); // транзакционный
        //    repo2.doSmth(); // транзакционный
        // }
    }

    @Test
    public void actuatorTest() {

    }

    @Test
    public void actuatorHeapDumpTest() {

    }
}
