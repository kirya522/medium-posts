package tech.kirya522.integrations.apis

import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.kirya522.integrations.events.WeatherPosterEvent
import tech.kirya522.integrations.services.WeatherPoster
import java.util.concurrent.CompletableFuture

@RestController("/")
class IntegrationController(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val weatherPoster: WeatherPoster
) {

    @PutMapping("sendMessageViaEvent")
    fun sendMessageViaEvent(
        @RequestParam(
            "msg",
            required = false,
            defaultValue = "Hi from integrations!"
        ) msg: String
    ) {
        applicationEventPublisher.publishEvent(WeatherPosterEvent(this, msg))
    }

    @PutMapping("sendMessageAsTask")
    fun sendMessageAsTask(@RequestParam("msg", required = false, defaultValue = "Hi from integrations!") msg: String) {

    }

    @PutMapping("sendMessageAsync")
    fun sendMessageAsync(@RequestParam("msg", required = false, defaultValue = "Hi from integrations!") msg: String) {
        CompletableFuture.runAsync { weatherPoster.sendMessage(msg) }
    }

    @PutMapping("/sendMessageViaDirectCall")
    fun sendMessageViaDirectCall(
        @RequestParam(
            "msg",
            required = false,
            defaultValue = "Hi from integrations!"
        ) msg: String
    ): Boolean {
        weatherPoster.sendMessage(msg)
        return true
    }
}