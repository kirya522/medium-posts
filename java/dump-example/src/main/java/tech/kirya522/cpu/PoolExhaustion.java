package tech.kirya522.cpu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolExhaustion {

    private final ExecutorService executor;

    public PoolExhaustion() {
        this.executor = Executors.newFixedThreadPool(4);
    }

    public void burn() {
        for (int i = 0; i < 100; i++) {
            handleRequest();
        }
        try {
            // httpClient.send(request, BodyHandlers.ofString());
            Thread.sleep(100_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleRequest() {
        executor.submit(() -> {
            try {
                Thread.sleep(10_000); // имитация blocking IO
            } catch (InterruptedException e) {
            }
        });
    }
}
