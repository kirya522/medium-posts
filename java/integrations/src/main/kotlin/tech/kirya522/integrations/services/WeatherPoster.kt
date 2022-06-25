package tech.kirya522.integrations.services

interface WeatherPoster {
    fun sendMessage(msg: String)

    fun sendMessageAsync(msg: String)
}