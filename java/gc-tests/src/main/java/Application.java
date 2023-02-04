import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Application implements Runnable {
    private final long numberOfOperations;
    private final int processors;
    private final ConcurrentLinkedQueue<Data> queue;

    public Application(long numberOfOperations,
                       int processors) {
        this.numberOfOperations = numberOfOperations;
        this.processors = processors;
        this.queue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {
        // pusher
        generatePushers(processors * 3);

        // consumer
        generatePullers(processors);

        while (true){

        }
    }

    private void generatePushers(int number) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int finalI = i;
            threads.add(new Thread(() -> {
                for (int j = 0; j < numberOfOperations / number; j++) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    queue.add(new Data((long) j * finalI + j, finalI, "Name_" + j, "Comment_" + j));
                }
                System.out.println("Pusher " + finalI + " finished");
            }, "pusher_" + i));
            threads.get(threads.size() - 1).start();
        }
    }

    private void generatePullers(int number) {
        for (int i = 0; i < number; i++) {
            new Thread(() -> {
                while (true) {
                    Data poll = queue.poll();
                    if (poll != null){
                        System.out.println(poll);
                    }
                }
            }, "consumer_" + i).start();
        }
    }
}
