package tech.kirya522.integrations.testconfigurations

import org.mockito.kotlin.mock
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.web.client.RestTemplate
import tech.kirya522.integrations.configurations.IntegrationsConfiguration

@TestConfiguration
@Import(IntegrationsConfiguration::class)
class WeatherPosterTestConfiguration {

    @Primary
    @Bean
    fun restTemplate(): RestTemplate {
        return mock()
    }
}