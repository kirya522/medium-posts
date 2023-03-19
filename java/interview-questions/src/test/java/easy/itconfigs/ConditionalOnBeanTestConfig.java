package easy.itconfigs;

import easy.testclasses.BadHashDistribution;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class ConditionalOnBeanTestConfig {

    @ConditionalOnProperty(value = "my.custom.value", havingValue = "true")
    @Bean
    public BadHashDistribution badHashDistribution(){
        return new BadHashDistribution("Condition 1");
    }

    @ConditionalOnMissingBean(value = BadHashDistribution.class)
    @Bean
    public BadHashDistribution otherbadHashDistribution(){
        return new BadHashDistribution("Condition 2");
    }
}
