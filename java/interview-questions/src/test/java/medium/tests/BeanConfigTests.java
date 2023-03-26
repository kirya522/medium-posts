package medium.tests;

import easy.itconfigs.BeanLifecycleBean;
import easy.testclasses.BadHashDistribution;
import medium.itconfigs.SimpleTestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = SimpleTestConfig.class)
@SpringBootTest
public class BeanConfigTests {

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private BeanLifecycleBean beanLifecycleBeanPrototype;

    @Autowired
    private BadHashDistribution badHashDistributionSingleton;

    @Test
    public void shouldInjectValue() {
        Assertions.assertNotNull(beanLifecycleBeanPrototype);
    }

    @Test
    public void shouldGetDifferentBeans() {
        BeanLifecycleBean bean = beanFactory.getBean(BeanLifecycleBean.class);
        Assertions.assertNotNull(beanLifecycleBeanPrototype);
        Assertions.assertNotEquals(bean, beanLifecycleBeanPrototype);
    }

    @Test
    public void shouldGetSameBeans() {
        BadHashDistribution bean = beanFactory.getBean(BadHashDistribution.class);
        Assertions.assertNotNull(badHashDistributionSingleton);
        Assertions.assertEquals(bean, badHashDistributionSingleton);
    }
}
