package medium.tests;

import medium.itconfigs.EventsTestConfig;
import medium.services.ApplicationStartedEventListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = EventsTestConfig.class)
@SpringBootTest
public class EventTests {

    @Autowired
    private ApplicationStartedEventListener eventListener;

    /**
     * https://www.baeldung.com/spring-events
     * https://www.tutorialspoint.com/spring/event_handling_in_spring.htm
     */
    @Test
    public void shouldPrintInfo(){
        Assertions.assertNotNull(eventListener);
        System.out.println(12);
    }
}
