package easy.tests;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class TestContainersIT {
    @Container
    private PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer();

    /**
     * https://www.testcontainers.org/test_framework_integration/junit_5/
     */
    @Test
    public void dockerTestContainers_Demo(){
        Assertions.assertThat(postgresqlContainer.isRunning()).isTrue();
    }
}
