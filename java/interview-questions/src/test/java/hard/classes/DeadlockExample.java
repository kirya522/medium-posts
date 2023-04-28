package hard.classes;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class DeadlockExample {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();
    private static final CyclicBarrier barrier = new CyclicBarrier(2);

    private static final CyclicBarrier barrier1 = new CyclicBarrier(2);
    private static final CyclicBarrier barrier2 = new CyclicBarrier(2);

    public void synchronizedDeadLock() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Thread 1 acquired lock1.");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println("Thread 1 acquired lock2.");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("Thread 2 acquired lock2.");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                    System.out.println("Thread 2 acquired lock1.");
                }
            }
        });
        System.out.println("Deadlock started");
        t1.start();
        t2.start();
        t2.join();
        t1.join();
        System.out.println("Deadlock released");
    }

    /**
     * https://itsobes.ru/JavaSobes/kak-poluchit-garantirovannyi-dedlok/
     */
    public void cyclicBarrierDeadLock() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                synchronized (lock1) {
                    System.out.println("Thread 1 acquired lock1");
                    barrier.await();
                    synchronized (lock2) {
                        System.out.println("Thread 1 acquired lock2");
                    }
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                synchronized (lock2) {
                    System.out.println("Thread 2 acquired lock2");
                    barrier.await();
                    synchronized (lock1) {
                        System.out.println("Thread 2 acquired lock1");
                    }
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Deadlock started");
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println("Deadlock released");
    }

    public void noNotificationDeadLock() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                barrier1.await();
                synchronized (barrier2) {
                    System.out.println("Thread 1 acquired barrier2.");
                    barrier2.await();
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                barrier2.await();
                synchronized (barrier1) {
                    System.out.println("Thread 2 acquired barrier1.");
                    barrier1.await();
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Deadlock started");
        t1.start();
        t2.start();
        t2.join();
        t1.join();
        System.out.println("Deadlock released");
    }

    public synchronized static void simplestDeadlock() {
        System.out.println("Deadlock started");
        try {
            Thread t = new Thread(DeadlockExample::simplestDeadlock);
            t.start();
            t.join();
        } catch (Exception ex) {}
        System.out.println("Deadlock released");
    }
}
