package tech.kirya522.integrations.events

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import tech.kirya522.integrations.services.WeatherPoster

@Component
class WeatherPosterEventListener(private val weatherPoster: WeatherPoster) {

    @Async
    @EventListener
    fun postWeatherEvent(weatherPosterEvent: WeatherPosterEvent){
        weatherPoster.sendMessage(weatherPosterEvent.msg)
    }
}