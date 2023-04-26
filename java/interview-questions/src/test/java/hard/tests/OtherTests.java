package hard.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://www.loggly.com/ultimate-guide/java-logging-basics/
 * https://www.baeldung.com/java-logging-intro
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

    @Test
    public void whatHappensWithLogs(){
        // msg -> Log -> appender -> queue -> processing -> logging storage
    }

    @Test
    public void unlimitedCacheLikeMap(){
        // map like cache
    }
}
