package hard.classes;

import medium.services.TestTransactionalService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public class ComplexTransactionalService {
    public final TestTransactionalService transactionalService;

    public ComplexTransactionalService(TestTransactionalService transactionalService) {
        this.transactionalService = transactionalService;
    }

    @Transactional
    public void executeInTransactionMakeAsyncUnderHood() {
        transactionalService.asyncTransactional();
    }

    @Async
    @Transactional
    public void asyncMethod() throws InterruptedException {
        transactionalService.asyncTransactional();
        transactionalService.longTransaction();

        System.out.println("async finished");
    }

    @Transactional
    public void regularTransaction() {
        transactionalService.transactionalAsync();
    }
}
