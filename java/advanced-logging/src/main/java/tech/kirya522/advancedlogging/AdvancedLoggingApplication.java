package tech.kirya522.advancedlogging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AdvancedLoggingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvancedLoggingApplication.class, args);
	}

}
