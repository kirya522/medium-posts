package tech.kirya522.integrations.events

import org.springframework.context.ApplicationEvent

class WeatherPosterEvent(source: Any, val msg: String) : ApplicationEvent(source) {
}