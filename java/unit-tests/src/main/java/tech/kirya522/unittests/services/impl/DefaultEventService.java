package tech.kirya522.unittests.services.impl;

import org.springframework.stereotype.Service;
import tech.kirya522.unittests.entities.Event;
import tech.kirya522.unittests.repositories.EventRepository;
import tech.kirya522.unittests.services.EventService;

@Service
public class DefaultEventService implements EventService {

    private final EventRepository eventRepository;

    public DefaultEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event findEvent(long id) {
        return eventRepository.findEventById(id);
    }

    @Override
    public void saveEvent(Event event) {
        eventRepository.saveEvent(event);
    }
}
