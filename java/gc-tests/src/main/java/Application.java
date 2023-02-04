import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class Application implements Runnable {
    private final long numberOfOperations;
    private final int pushersNumber;
    private final ConcurrentLinkedQueue<Data> queue;

    public Application(long numberOfOperations,
                       int pushersNumber) {
        this.numberOfOperations = numberOfOperations;
        this.pushersNumber = pushersNumber;
        this.queue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {
        // pusher
        generatePushers(pushersNumber);

        // consumer
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Data poll = queue.poll();
                System.out.println(poll);
            }
        }, "consumer").start();

        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void generatePushers(int number) {
        for (int i = 0; i < number; i++) {
            int finalI = i;
            new Thread(() -> {
                for (int j = 0; j < numberOfOperations / number; j++) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    queue.add(new Data((long) j * finalI + j, "Name_" + j, "Comment_" + j));
                }
                System.out.println("Pusher " + finalI + " finished");
            }, "pusher_" + i).start();
        }
    }
}
