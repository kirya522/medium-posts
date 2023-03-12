import org.junit.Test;

public class EasyQuestionsTests {

    @Test
    public void primitivesByValue_Demo() {

    }

    @Test
    public void objectsByValue_Demo() {

    }

    @Test
    public void hashMapEqualsHashCode_Demo() {

    }

    @Test
    public void hashMapBadDistribution_Demo() {

    }

    @Test
    public void collectionFramework_Demo() {

    }

    @Test
    public void checkedException_Demo() {

    }

    @Test
    public void unCheckedException_Demo() {

    }

    @Test
    public void optionalOf_Demo() {

    }

    @Test
    public void optionalOfNullable_Demo() {

    }

    /**
     * More info https://www.baeldung.com/java-threadlocal
     */
    @Test
    public void threadLocal_Demo() throws InterruptedException {
        Runnable shared = new Runnable() {
            private final ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

            @Override
            public void run() {
                System.out.println("before: " + threadLocal.get());

                threadLocal.set((int) (Math.random() * 100D));

                try {
                    // emulate workload
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }

                System.out.println("after: " + threadLocal.get());
            }
        };

        // concurrent access to data, datarace issues
        Thread first = new Thread(shared, "first");
        Thread second = new Thread(shared, "second");

        // happens before
        first.start();
        second.start();

        first.join();
        second.join();
    }

    /**
     * More info https://jenkov.com/tutorials/java-concurrency/threadlocal.html
     */
    @Test
    public void inheritableThreadLocal_Demo() throws InterruptedException {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        InheritableThreadLocal<String> inheritableThreadLocal =
                new InheritableThreadLocal<>();

        Thread thread1 = new Thread(() -> {
            System.out.println("===== Thread 1 =====");
            threadLocal.set("Thread 1 - ThreadLocal");
            inheritableThreadLocal.set("Thread 1 - InheritableThreadLocal");

            System.out.println(threadLocal.get());
            System.out.println(inheritableThreadLocal.get());

            Thread childThread = new Thread(() -> {
                System.out.println("===== ChildThread =====");
                System.out.println(threadLocal.get());
                System.out.println(inheritableThreadLocal.get());
            });
            childThread.start();
            try {
                childThread.join();
            } catch (InterruptedException ignored) {
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("===== Thread2 =====");
            System.out.println(threadLocal.get());
            System.out.println(inheritableThreadLocal.get());
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    @Test
    public void streams_Demo() {

    }

    @Test
    public void primitiveStreams_Demo() {

    }

    @Test
    public void tryCatchFinally_Demo() {

    }

    @Test
    public void tryWithResource_Demo() {

    }
}
