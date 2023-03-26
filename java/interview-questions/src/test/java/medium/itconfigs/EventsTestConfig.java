package medium.itconfigs;


import easy.testclasses.BadHashDistribution;
import medium.services.ApplicationStartedEventListener;
import medium.services.TestTransactionalService;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

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
