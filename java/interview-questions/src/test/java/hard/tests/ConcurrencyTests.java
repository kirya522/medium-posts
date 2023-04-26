package hard.tests;

import hard.classes.AtomicIntegerExample;
import hard.classes.ReadWriteLockExample;
import hard.classes.ReentrantLockExample;
import hard.classes.SynchronizedExample;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class ConcurrencyTests {
    /**
     * https://www.baeldung.com/java-when-to-use-parallel-stream
     * https://www.javatpoint.com/parallel-stream-in-java
     * https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html
     */
    @Test
    public void parallelStreamBasic() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        numbers.parallelStream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * n)
                .forEach(System.out::println);
    }

    @Test
    public void parallelUndefinedBehaviour() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        numbers.parallelStream()
                .filter(n -> n % 2 == 0)
                // potentially failed call
                .map(n -> new RestTemplate().getForObject("https://google.com", String.class))
                .forEach(System.out::println);
    }

    /**
     * https://www.baeldung.com/java-8-parallel-streams-custom-threadpool
     * https://medium.com/geekculture/pitfalls-of-java-parallel-streams-731fe0c1eb5f
     */
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

    /**
     * https://www.geeksforgeeks.org/lock-framework-vs-thread-synchronization-in-java/
     * https://winterbe.com/posts/2015/04/30/java8-concurrency-tutorial-synchronized-locks-examples/
     * https://www.baeldung.com/java-concurrent-locks
     * https://jenkov.com/tutorials/java-concurrency/read-write-locks.html
     */
    @Test
    public void synchronizedVsLocksTest() {
        ReentrantLockExample lockExample = new ReentrantLockExample();
        SynchronizedExample synchronizedExample = new SynchronizedExample();
        AtomicIntegerExample atomicIntegerExample = new AtomicIntegerExample();
        ReadWriteLockExample readWriteLockExample = new ReadWriteLockExample();

        ForkJoinPool customThreadPool = new ForkJoinPool(4);

        for (int i = 0; i < 10; i++) {
            customThreadPool.submit(() -> {
                lockExample.increment();
                System.out.println("lock =" + lockExample.getCount());
                synchronizedExample.increment();
                System.out.println("synchronized =" + synchronizedExample.getCount());
                atomicIntegerExample.increment();
                System.out.println("atomic =" + atomicIntegerExample.getCount());
                readWriteLockExample.increment();
                System.out.println("readWrite =" + readWriteLockExample.getCount());
            });
        }

        customThreadPool.shutdown();
    }

    @Test
    public void happensBeforeForFinal() {

    }

    @Test
    public void happensBeforeForVolatile() {

    }
}
