package hard.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

public class BeanB {
    public final BeanA beanA;

    @Autowired
    public BeanB(@Lazy BeanA beanA) {
        this.beanA = beanA;
    }

    public String getName() {
        return "BeanB";
    }
}
