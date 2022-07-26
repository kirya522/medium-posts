package tech.kirya522.integrations.services

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Metrics
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.retry.support.RetryTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate
import tech.kirya522.integrations.testconfigurations.WeatherPosterTestConfiguration

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [WeatherPosterTestConfiguration::class])
internal class WeatherPosterImplIT {

    private val URL = "https://goweather.herokuapp.com/weather/"
    private val MESSAGE = "msg"
    private val meterRegistry: MeterRegistry = Metrics.globalRegistry

    @Autowired
    private lateinit var restTemplateMock: RestTemplate

    @Autowired
    private lateinit var retryTemplate: RetryTemplate


    private lateinit var weatherPosterImpl: WeatherPosterImpl

    @BeforeEach
    internal fun setUp() {
        Metrics.addRegistry(SimpleMeterRegistry())

        weatherPosterImpl = WeatherPosterImpl(restTemplateMock, retryTemplate)
    }

    @AfterEach
    internal fun tearDown() {
        meterRegistry.clear()
        Mockito.reset(restTemplateMock)
    }

    @Test
    fun sendMessage_shouldBeRetried_whenRequestFailed() {
        whenever(restTemplateMock.getForObject<String>(eq("${URL}${MESSAGE}"), any()))
            .thenThrow(RuntimeException::class.java)
            .thenThrow(RuntimeException::class.java)
            .thenReturn("response")

        weatherPosterImpl.sendMessage(MESSAGE)

        verify(restTemplateMock, times(3)).getForObject<String>(eq("${URL}${MESSAGE}"), any())
        val retriesTracked = meterRegistry.get("weather_retries_counter").counter().count().toInt()
        assertEquals(2, retriesTracked)
    }

}