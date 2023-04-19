package medium.itconfigs;


import easy.itconfigs.BeanLifecycleBean;
import easy.testclasses.BadHashDistribution;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

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
