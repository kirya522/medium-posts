package medium.tests;

import medium.classes.VolatileExample;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.parallel.Execution;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

/**
 * https://wuciawe.github.io/jvm/2017/02/13/notes-on-concurrency-in-jvm.html
 * https://habr.com/ru/company/otus/blog/353414/
 * https://www.youtube.com/watch?v=CgRJUqO-dMQ
 * https://www.youtube.com/watch?v=arGcok_I_DY&list=WL&index=7
 * https://www.youtube.com/watch?v=mf4lC6TpclM
 * https://www.youtube.com/watch?v=noDnSV7NCtw
 * https://www.youtube.com/watch?v=Ky1_5mabd18
 */
public class ConcurrencyTests {

    @Execution(CONCURRENT)
    @RepeatedTest(10)
    public void volatileTests() throws InterruptedException {
        var volatileExample = new VolatileExample();

        volatileExample.doTheWorkInParallel();
    }

    /**
     * https://medium.com/@basecs101/difference-between-atomic-volatile-and-synchronized-in-java-programming-14156c3c8f46
     * https://www.baeldung.com/java-wait-notify
     * https://web.archive.org/web/20220628134440/https://habr.com/ru/post/143237/
     */
    @Execution(CONCURRENT)
    @RepeatedTest(10)
    public void volatilePlusPlusTests() throws InterruptedException {
        var volatileExample = new VolatileExample();

        volatileExample.doTheWorkInParallelVolatile();
    }

    /**
     * https://www.baeldung.com/java-volatile
     * https://www.javatpoint.com/volatile-keyword-in-java
     * https://habr.com/ru/post/685518/
     * https://habr.com/ru/company/golovachcourses/blog/221133/
     * https://web.archive.org/web/20220519121335/https://habr.com/en/post/133981/
     */
    @Execution(CONCURRENT)
    @RepeatedTest(10)
    public void realVolatileTests() {
        var volatileExample = new VolatileExample();

        volatileExample.realVolatileUsage();
    }


    /**
     * https://www.baeldung.com/lock-free-programming
     * https://habr.com/ru/post/319036/
     * https://www.youtube.com/watch?v=XivoUctdPIU
     */
    @Execution(CONCURRENT)
    @RepeatedTest(10)
    public void atomicTests() throws InterruptedException {
        var volatileExample = new VolatileExample();

        volatileExample.doTheWorkInParallelAtomicInteger();
    }

    /**
     * https://www.youtube.com/watch?v=kwS3OeoVCno&ab_channel=JPoint,Joker%D0%B8JUGru
     * https://www.geeksforgeeks.org/difference-between-java-threads-and-os-threads/
     * https://medium.com/@unmeshvjoshi/how-java-thread-maps-to-os-thread-e280a9fb2e06
     * https://www.baeldung.com/java-start-thread
     */
    @Execution(CONCURRENT)
    @RepeatedTest(10)
    public void startThreadTests() {
        // 1
        new Thread() {
            @Override
            public void run() {
                super.run();
                System.out.println(1);
            }
        }.start();

        // 2
        new Thread(() -> System.out.println(2)).start();

        // 3
        ExecutorService executor = Executors.newFixedThreadPool(10);
        executor.execute(() -> System.out.println(3));

        // 4
        CompletableFuture.runAsync(() -> System.out.println(4));
    }
}
