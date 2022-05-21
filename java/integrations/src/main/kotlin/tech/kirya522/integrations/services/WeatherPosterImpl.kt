package tech.kirya522.integrations.services

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Metrics
import org.apache.commons.lang.StringEscapeUtils
import org.slf4j.LoggerFactory
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class WeatherPosterImpl(
    private val restTemplate: RestTemplate, private val retryTemplate: RetryTemplate
) : WeatherPoster {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(WeatherPosterImpl::class.java)

        private const val SERVICE_URL_TEMPLATE = "https://goweather.herokuapp.com/weather/"

        private val RETRIES_COUNTER: Counter =
            Counter.builder("${WeatherPoster::javaClass.name}_retries_counter").register(
                Metrics.globalRegistry
            )
    }

    override fun sendMessage(msg: String) {
        try {
            Thread.sleep(1000)
            val response = retryTemplate.execute<String, RuntimeException> {
                if (it.retryCount > 0) {
                    RETRIES_COUNTER.increment()
                }
                StringEscapeUtils.unescapeJava(
                    restTemplate.getForObject<String>(
                        "${SERVICE_URL_TEMPLATE}${msg}"
                    )
                )
            }
            LOGGER.info("Weather response for ${msg}, current retries: ${RETRIES_COUNTER.count()}:\n  ${response}")
        } catch (ex: RuntimeException) {
            LOGGER.error("Issue happened, number of retries = ${RETRIES_COUNTER.count()}", ex)
        }
    }
}