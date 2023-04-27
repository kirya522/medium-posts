package easy.tests;


import easy.itconfigs.ConditionalOnBeanTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = {ConditionalOnBeanTestConfig.class})
@Testcontainers
public class TestContainersIT {
    private final static String POSTGRES_VERSION="postgres:13.10";
    @Container
    private PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer(POSTGRES_VERSION);

    /**
     * https://www.testcontainers.org/test_framework_integration/junit_5/
     * https://www.docker.com/
     */
    @Test
    public void dockerTestContainers_Demo(){
        Assertions.assertThat(postgresqlContainer.isRunning()).isTrue();
    }
}
