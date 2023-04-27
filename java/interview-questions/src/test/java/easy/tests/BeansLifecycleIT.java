package easy.tests;


import easy.itconfigs.BeanLifecycleBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {BeanLifecycleBean.class})
@SpringBootTest
public class BeansLifecycleIT {

    @Autowired
    private BeanLifecycleBean bean;

    /**
     * https://www.geeksforgeeks.org/bean-life-cycle-in-java-spring/
     */
    @Test
    public void beansLifecycle_Demo(){
        Assertions.assertNotNull(bean);
        Assertions.assertEquals("inited", bean.getName());
    }
}
