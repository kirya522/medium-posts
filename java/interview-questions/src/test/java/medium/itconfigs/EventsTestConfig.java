package medium.itconfigs;


import medium.services.ApplicationStartedEventListener;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

/**
 * Read more
 * https://www.baeldung.com/spring-events
 */
public class EventsTestConfig {

    @Bean
    public ApplicationStartedEventListener applicationStartedEventListener(){
        return new ApplicationStartedEventListener();
    }
}
