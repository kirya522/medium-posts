package hard.tests;

import hard.classes.DeadlockExample;
import hard.classes.NestedLocksExample;
import hard.classes.TryLockExample;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * https://www.baeldung.com/java-deadlock-livelock
 * https://www.javatpoint.com/deadlock-in-java
 * https://www.digitalocean.com/community/tutorials/deadlock-in-java-example
 */
public class DeadLocksTests {

    @Timeout(5)
    @Test
    public void nestedLocksDeadLock() throws InterruptedException {
        // Collect thread dump
        DeadlockExample deadlockExample = new DeadlockExample();

        deadlockExample.synchronizedDeadLock();
    }

    @Timeout(5)
    @Test
    public void cyclicBarrier() throws InterruptedException {
        DeadlockExample deadlockExample = new DeadlockExample();

        deadlockExample.cyclicBarrierDeadLock();
    }

    @Test
    public void nestedLocksCorrect() {
        NestedLocksExample example = new NestedLocksExample();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                example.method1();
                example.method2();
            }).start();
        }
        System.out.println("No dead locks");
    }

    @Test
    public void nestedLocksTryLock() {
        TryLockExample tryLockExample = new TryLockExample();
        new Thread(() -> {
            try {
                tryLockExample.method1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                tryLockExample.method2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        System.out.println("Deadlock resolved");
    }
}
