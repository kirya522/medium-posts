package tech.kirya522;

import tech.kirya522.cpu.CpuBurner;
import tech.kirya522.cpu.DeadLock;
import tech.kirya522.cpu.PoolExhaustion;
import tech.kirya522.mem.MemBurner;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static void main() throws InterruptedException {
        // Режим работы для задачи
        int mode = 1;

        List<Thread> threads = new ArrayList<>();
        switch (mode) {
            // just cycle
            case 1:
                // jps
                // jstack <pid>
                // Профилировщик делает это 10000 раз в секунду
                var thread1 = new Thread(() -> new CpuBurner().burn(), "cpu");
                threads.add(thread1);
                thread1.start();
                break;

            // bad infra
            case 2:
                // jps
                // jstack <pid>
                // Профилировщик делает это 10000 раз в секунду
                var thread2 = new Thread(() -> new CpuBurner().burnLog(), "cpu_log");
                threads.add(thread2);
                thread2.start();
                break;

            // blocking io
            case 3:
                // jstack <pid>
                // TIMED_WAITING
                var thread3 = new Thread(() -> new PoolExhaustion().burn(), "pool");
                threads.add(thread3);
                thread3.start();
                break;

            // oom
            case 4:
                // -XX:+HeapDumpOnOutOfMemoryError
                // mat
                // -Xmx512m
                // shallow - object itself
                // retained - all the dependencies and tree
                // real example - huge excel file -> server OOM
                // copy on write array
                var thread4 = new Thread(() -> new MemBurner().burn(), "map_leak");
                var thread5 = new Thread(() -> new MemBurner().notBurn(), "cache_not_leak");
                thread4.start();
                thread5.start();
                threads.add(thread4);
                threads.add(thread5);
                break;

            // prod leak
            case 5:
                // https://dev.to/faangmaster/java-thread-local-memory-leaks-2d28
                var thread6 = new Thread(() -> new MemBurner().leak(), "thread_local_leak");
                thread6.start();
                threads.add(thread6);

                // no_payload, paging, limit
                var thread7 = new Thread(() -> new MemBurner().streamLeak(), "stream_leak");
                thread7.start();
                threads.add(thread7);
                break;

            // deadlock
            case 6:
                // BLOCKED
                var thread8 = new Thread(() -> new DeadLock().deadlock(), "deadlock");
                thread8.start();
                threads.add(thread8);

                var thread9 = new Thread(() -> new DeadLock().deadlockSolution(), "no_deadlock");
                thread9.start();
                threads.add(thread9);
                break;
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}
