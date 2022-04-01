package tech.kirya522.unittests.dtos.event;

import tech.kirya522.unittests.entities.Event;

public interface EventDTOBuilder {
    EventDTO fromEvent(Event event);

    Event fromEventDTO(EventDTO eventDTO);
}
