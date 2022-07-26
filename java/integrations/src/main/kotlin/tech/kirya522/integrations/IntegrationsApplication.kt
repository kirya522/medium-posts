package tech.kirya522.integrations

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class IntegrationsApplication

fun main(args: Array<String>) {
	runApplication<IntegrationsApplication>(*args)
}
