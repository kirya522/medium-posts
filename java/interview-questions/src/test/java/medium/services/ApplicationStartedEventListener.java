package medium.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;

public class ApplicationStartedEventListener  {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartedEventListener.class);
    @EventListener(classes = {ContextRefreshedEvent.class, ContextStartedEvent.class})
    public void onContextStarted() {
        LOGGER.info("APPLICATION STARTED AT PORT=1337");
    }
}
