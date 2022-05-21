package tech.kirya522.integrations.services

interface WeatherPoster {
    fun sendMessage(msg: String)
}