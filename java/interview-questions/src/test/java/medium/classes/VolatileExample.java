package medium.classes;

import java.util.concurrent.atomic.AtomicInteger;

public class VolatileExample {
    private int value = 0;

    private volatile int volatileValue = 0;

    private AtomicInteger atomicInteger = new AtomicInteger(0);


    public void doTheWorkInParallel() throws InterruptedException {
        int repeatTime = 1000;
        int numberOfThreads = 100;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < repeatTime; i++) {
                    value++;
                }
            }
        };

        Thread workers[] = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            workers[i] = new Thread(runnable, String.valueOf(i));
            workers[i].start();
        }
        for (int j = 0; j < numberOfThreads; j++) {
            workers[j].join(); //todo add catch exception
        }

        System.out.printf("value = %d expected = %d", value, repeatTime * numberOfThreads);
        assert value == repeatTime * numberOfThreads;
    }

    public void doTheWorkInParallelVolatile() throws InterruptedException {
        int repeatTime = 1000;
        int numberOfThreads = 1000;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < repeatTime; i++) {
                    volatileValue++;
                }
            }
        };

        Thread workers[] = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            workers[i] = new Thread(runnable, String.valueOf(i));
            workers[i].start();
        }
        for (int j = 0; j < numberOfThreads; j++) {
            workers[j].join(); //todo add catch exception
        }

        System.out.printf("value = %d expected = %d", volatileValue, repeatTime * numberOfThreads);
        assert volatileValue == repeatTime * numberOfThreads;
    }

    public void doTheWorkInParallelAtomicInteger() throws InterruptedException {
        int repeatTime = 1000;
        int numberOfThreads = 1000;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < repeatTime; i++) {
                    atomicInteger.incrementAndGet();
                }
            }
        };

        Thread workers[] = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            workers[i] = new Thread(runnable, String.valueOf(i));
            workers[i].start();
        }
        for (int j = 0; j < numberOfThreads; j++) {
            workers[j].join(); //todo add catch exception
        }

        System.out.printf("value = %d expected = %d", atomicInteger.get(), repeatTime * numberOfThreads);
        assert atomicInteger.get() == repeatTime * numberOfThreads;
    }

    public void realVolatileUsage(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                value = 1;
                volatileValue = 1;
            }
        }).start();

        // copy values, because on else other thread may apply values
        int valueTemp = value;
        int volatileValueTemp = volatileValue;

        if(volatileValueTemp == 1){
            assert valueTemp == 1;
        } else {
            assert valueTemp == 0;
        }
    }
}
