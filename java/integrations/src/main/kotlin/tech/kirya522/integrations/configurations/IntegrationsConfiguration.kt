package tech.kirya522.integrations.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import org.springframework.web.client.RestTemplate


@Configuration
class IntegrationsConfiguration {

    @Bean
    fun retryTemplate(): RetryTemplate {

        val retryTemplate = RetryTemplate()

        val exponentialBackOffPolicy = ExponentialBackOffPolicy()
        exponentialBackOffPolicy.initialInterval = 100L
        exponentialBackOffPolicy.maxInterval = 2000L
        exponentialBackOffPolicy.multiplier = 10.0
        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy)

        val retryPolicy = SimpleRetryPolicy()
        retryPolicy.maxAttempts = 3
        retryTemplate.setRetryPolicy(retryPolicy)

        return retryTemplate
    }

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}