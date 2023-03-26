package medium.itconfigs;


import easy.itconfigs.BeanLifecycleBean;
import easy.testclasses.BadHashDistribution;
import medium.services.TestTransactionalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import static org.mockito.Mockito.mock;

/**
 * Read more
 * https://www.baeldung.com/spring-bean
 * https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-definition
 */
@EnableTransactionManagement
public class TransactionalTestConfig {
    public BadHashDistribution badHashDistribution(){
        return new BadHashDistribution("1");
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        return mock(PlatformTransactionManager.class);
    }

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager){
        return new TransactionTemplate(transactionManager);
    }
    
    /**
     * https://javarevisited.blogspot.com/2021/08/spring-transactional-example-how-to.html
     */
    @Bean
    public TestTransactionalService testTransactionalService(TransactionTemplate transactionTemplate){
        return new TestTransactionalService("beanName", transactionTemplate);
    }
}
