package hard.tests;

import hard.classes.SimpleCacheExample;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * https://www.loggly.com/ultimate-guide/java-logging-basics/
 * https://www.baeldung.com/java-logging-intro
 * https://www.baeldung.com/spring-boot-logging
 * https://www.baeldung.com/log4j2-appenders-layouts-filters
 * https://coralogix.com/blog/spring-boot-logging-best-practices-guide/
 * https://www.marcobehler.com/guides/java-logging
 * https://youtu.be/Jdc7Qu4p624
 * https://youtube.com/live/wCAKq1l-Zbw
 */
public class OtherTests {

    /**
     * Why logger is static?
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OtherTests.class);

    @Timeout(5)
    @Test
    public void whileTrueLogger() {
        int i = 0;
        while (true) {
            LOGGER.info("New message = " + i++);
        }
    }

    /**
     * https://medium.com/@lyzkevin2020/how-to-build-a-log-collection-system-for-springboot-projects-in-kubernetes-3f4c3e09dd6b
     * https://www.alibabacloud.com/blog/how-log-service-has-evolved-into-a-data-pipeline-over-the-past-5-years_596112
     */
    @Test
    public void whatHappensWithLogs() {
        // msg -> Log -> appender -> queue -> processing -> logging storage
    }

    @Test
    public void unlimitedCacheLikeMap() {
        // map like cache
        SimpleCacheExample<String, String> cacheExample = new SimpleCacheExample<>(new HashMap<>());

        for (int i = 0; i < 10000; i++) {
            cacheExample.save("a" + i, "b");
            cacheExample.get("a" + (i - 1));
        }

        System.out.println(cacheExample.size());
    }

    @Test
    public void unlimitedConcurrentCacheLikeMap() {
        // map like cache
        SimpleCacheExample<String, String> concurrentCacheExample = new SimpleCacheExample<>(new ConcurrentHashMap<>());

        for (int i = 0; i < 10000; i++) {
            concurrentCacheExample.save("a" + i, "b");
            concurrentCacheExample.get("a" + (i - 1));
        }

        System.out.println(concurrentCacheExample.size());
    }
}
