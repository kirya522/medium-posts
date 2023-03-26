package medium.services;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class TestTransactionalService {
    private final String beanName;
    private final TransactionTemplate transactionTemplate;

    public TestTransactionalService(String beanName,
                                    TransactionTemplate transactionTemplate) {
        this.beanName = beanName;
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * https://www.baeldung.com/transaction-configuration-with-jpa-and-spring
     * https://www.marcobehler.com/guides/spring-transaction-management-transactional-in-depth
     * TransactionAspectSupport for more details
     * https://www.baeldung.com/spring-programmatic-transaction-management
     */
    @Transactional
    public void createInTransaction(){
        int a = 1;
        int b = 2;
        privateTransaction();
        System.out.println(a+b);
        transactionTemplate.execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                privateTransaction();
            }
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void privateTransaction(){
        int c = 1;
        int d = 3;
        System.out.println(c+d);
    }

    /**
     * https://vladmihalcea.com/spring-transaction-best-practices/
     * https://medium.com/javarevisited/spring-core-managing-transactions-effectively-781bba6c47e8
     */
    @Transactional
    public void longTransaction() throws InterruptedException {
        int c = 1;
        int d = 3;
        Thread.sleep(10000);
        System.out.println(c+d);
    }
}
