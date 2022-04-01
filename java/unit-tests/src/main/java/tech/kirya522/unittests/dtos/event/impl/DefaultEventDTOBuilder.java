package tech.kirya522.unittests.dtos.event.impl;

import org.springframework.stereotype.Component;
import tech.kirya522.unittests.dtos.event.EventDTO;
import tech.kirya522.unittests.dtos.event.EventDTOBuilder;
import tech.kirya522.unittests.entities.Event;

@Component
public class DefaultEventDTOBuilder implements EventDTOBuilder {

    public EventDTO fromEvent(Event event) {
        return new EventDTO() {{
            setId(event.getId());
            setName(event.getName());
            setStartDate(event.getStartDate());
            setEndDate(event.getEndDate());
        }};
    }

    @Override
    public Event fromEventDTO(EventDTO eventDTO) {
        return new Event(eventDTO.getId(),
                eventDTO.getName(),
                eventDTO.getStartDate(),
                eventDTO.getEndDate()
        );
    }
}
