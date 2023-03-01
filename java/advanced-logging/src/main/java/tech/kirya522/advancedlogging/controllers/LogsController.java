package tech.kirya522.advancedlogging.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logs")
public class LogsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogsController.class);

    @GetMapping
    public boolean getResult(){
        LOGGER.trace("trace");
        LOGGER.debug("debug");
        LOGGER.info("info");
        LOGGER.warn("warn");
        LOGGER.error("error");

        return true;
    }
}
