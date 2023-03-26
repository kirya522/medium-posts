package medium.tests;

import medium.itconfigs.TransactionalTestConfig;
import medium.services.TestTransactionalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = TransactionalTestConfig.class)
@SpringBootTest
public class TransactionalTests {

    @Autowired
    private BeanFactory beanFactory;

    @Test
    public void shouldGetProxy() {
        TestTransactionalService bean = beanFactory.getBean(TestTransactionalService.class);
        Assertions.assertNotNull(bean);
        bean.createInTransaction();
    }
}
