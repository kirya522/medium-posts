package tech.kirya522.logs.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LogsPoster {
    private final static Logger LOGGER = LoggerFactory.getLogger(LogsPoster.class);

    @Scheduled(fixedRate=1000)
    public void postTrace(){
        LOGGER.trace("Trace message");
    }

    @Scheduled(fixedRate=1000)
    public void postDebug(){
        LOGGER.debug("Debug message");
    }

    @Scheduled(fixedRate=1000)
    public void postInfo(){
        LOGGER.info("Info message");
    }

    @Scheduled(fixedRate=1000)
    public void postWarn(){
        LOGGER.warn("Warn message");
    }

    @Scheduled(fixedRate=1000)
    public void postError(){
        LOGGER.error("Error message");
    }
}
