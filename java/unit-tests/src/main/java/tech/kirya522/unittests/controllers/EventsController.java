package tech.kirya522.unittests.controllers;

import org.springframework.web.bind.annotation.*;
import tech.kirya522.unittests.dtos.event.EventDTO;
import tech.kirya522.unittests.dtos.event.EventDTOBuilder;
import tech.kirya522.unittests.entities.Event;
import tech.kirya522.unittests.services.EventService;

@RestController
@RequestMapping("event")
public class EventsController {
    private final EventDTOBuilder eventDTOBuilder;
    private final EventService eventService;

    public EventsController(EventDTOBuilder eventDTOBuilder, EventService eventService) {
        this.eventDTOBuilder = eventDTOBuilder;
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public EventDTO getEvent(@PathVariable("id") long eventId) {
        final Event event = eventService.findEvent(eventId);
        return eventDTOBuilder.fromEvent(event);
    }

    @PutMapping
    public void saveEvent(@RequestBody EventDTO eventDTO) {
        final Event event = eventDTOBuilder.fromEventDTO(eventDTO);
        eventService.saveEvent(event);
    }
}
