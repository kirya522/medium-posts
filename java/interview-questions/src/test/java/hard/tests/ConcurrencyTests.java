package hard.tests;

import hard.classes.AtomicIntegerExample;
import hard.classes.ReentrantLockExample;
import hard.classes.SynchronizedExample;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class ConcurrencyTests {
    @Test
    public void parallelStreamBasic() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        numbers.parallelStream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * n)
                .forEach(System.out::println);
    }

    @Test
    public void parallelStreamWithSpecifiedPool() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        ForkJoinPool customThreadPool = new ForkJoinPool(4);

        customThreadPool.submit(() ->
                numbers.parallelStream()
                        .filter(n -> n % 2 == 0)
                        .map(n -> n * n)
                        .forEach(System.out::println)
        ).join();

        customThreadPool.shutdown();
    }

    @Test
    public void synchronizedVsLocksTest() {
        ReentrantLockExample lockExample = new ReentrantLockExample();
        SynchronizedExample synchronizedExample = new SynchronizedExample();
        AtomicIntegerExample atomicIntegerExample = new AtomicIntegerExample();

        ForkJoinPool customThreadPool = new ForkJoinPool(4);

        for (int i = 0; i < 10; i++) {
            customThreadPool.submit(() -> {
                lockExample.increment();
                System.out.println("lock =" + lockExample.getCount());
                synchronizedExample.increment();
                System.out.println("synchronized =" + synchronizedExample.getCount());
                atomicIntegerExample.increment();
                System.out.println("atomic =" + atomicIntegerExample.getCount());
            });
        }

        customThreadPool.shutdown();
    }
}
