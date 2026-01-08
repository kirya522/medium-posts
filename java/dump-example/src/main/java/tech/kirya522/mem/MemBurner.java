package tech.kirya522.mem;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MemBurner {
    private boolean burning = true;
    private final ConcurrentHashMap<Long, User> cache;

    private final Cache<Long, User> validCache;


    public MemBurner() {
        cache = new ConcurrentHashMap<>();
        validCache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofMinutes(5)).build();
    }

    public void burn() {
        for (int i = 0; burning; i++) {
            getValue(i);
        }
    }

    public void leak() {
        ExecutorService pool = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            pool.submit(ThreadLocalLeak::leak);
        }
    }

    public long streamLeak() {
        List<Order> orders = getOrders(10_000);

        var ordersDto = orders.stream()
                .map(OrderDto::new)
                .toList();

        try {
            Thread.sleep(30_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return ordersDto.size();
    }

    private List<Order> getOrders(int count) {
        List<Order> orders = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            orders.add(new Order(i));
        }
        return orders;
    }

    public void notBurn() {
        for (int i = 0; burning; i++) {
            validCache.get((long) i, k -> new User((long) Math.sqrt(System.nanoTime()), "username", 1999));

            try {
                TimeUnit.NANOSECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setBurning(boolean burning) {
        this.burning = burning;
    }

    public User getValue(long key) {
        return cache.computeIfAbsent(key, k -> new User((long) Math.sqrt(System.nanoTime()), "username", 1999));
    }
}
