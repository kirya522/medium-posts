package easy.itconfigs;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class BeanLifecycleBean {

    private String name;

    public BeanLifecycleBean() {
        this.name = "1";
    }

    @PostConstruct
    public void init(){
        System.out.println("Inited " + BeanLifecycleBean.class.getName());
        this.name = "inited";
    }

    @PreDestroy
    public void shutdown(){
        System.out.println("Inited " + BeanLifecycleBean.class.getName());
        this.name = "shutdown";
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "BeanLifecycleTestClass{" +
                "name='" + name + '\'' +
                '}';
    }
}
