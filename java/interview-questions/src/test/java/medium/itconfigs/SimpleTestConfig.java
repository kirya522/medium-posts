package medium.itconfigs;


import easy.itconfigs.BeanLifecycleBean;
import easy.testclasses.BadHashDistribution;
import medium.services.TestTransactionalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import static org.mockito.Mockito.mock;

/**
 * Read more
 * https://www.baeldung.com/spring-bean
 * https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-definition
 */
public class SimpleTestConfig {

    /**
     *  https://www.baeldung.com/spring-bean-scopes
     *  https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-factory-scopes
     */
    @Bean
    @Scope(scopeName = "prototype")
    public BeanLifecycleBean beanLifecycleBean(){
        return new BeanLifecycleBean();
    }

    /**
     *  https://www.baeldung.com/spring-bean-scopes
     *  https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-factory-scopes
     */
    @Bean
    @Scope(scopeName = "singleton")
    public BadHashDistribution badHashDistribution(){
        return new BadHashDistribution("1");
    }
}
