package hard.classes;

import org.springframework.context.annotation.Lazy;

public class BeanA {
    public final BeanB beanB;

    public BeanA(@Lazy BeanB beanB) {
        this.beanB = beanB;
    }

    public String getName() {
        return "BeanA";
    }
}
