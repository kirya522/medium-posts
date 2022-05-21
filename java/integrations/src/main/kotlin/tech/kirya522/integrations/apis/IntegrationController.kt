package tech.kirya522.integrations.apis

import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.kirya522.integrations.services.WeatherPoster

@RestController("/")
class IntegrationController(private val weatherPoster: WeatherPoster) {

    @PutMapping("sendMessageViaEvent")
    fun sendMessageViaEvent(
        @RequestParam(
            "msg",
            required = false,
            defaultValue = "Hi from integrations!"
        ) msg: String
    ) {

    }

    @PutMapping("sendMessageAsTask")
    fun sendMessageAsTask(@RequestParam("msg", required = false, defaultValue = "Hi from integrations!") msg: String) {

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