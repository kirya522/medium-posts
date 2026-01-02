package tech.kirya522.mem;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

public class MemBurner {
    private boolean burning = true;
    private final ConcurrentHashMap<Long, Long> cache;

    private final Cache<Long, Long> validCache;


    public MemBurner() {
        cache = new ConcurrentHashMap<>();
        validCache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofMinutes(5)).build();
    }

    public void burn() {
        for (int i = 0; burning; i++) {
            getValue(i);

            // delay
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void notBurn() {
        for (int i = 0; burning; i++) {
            validCache.get((long) i, k -> (long) Math.sqrt(System.nanoTime()));

            // delay
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setBurning(boolean burning) {
        this.burning = burning;
    }

    public Long getValue(long key) {
        return cache.computeIfAbsent(key, k -> (long) Math.sqrt(System.nanoTime()));
    }
}
