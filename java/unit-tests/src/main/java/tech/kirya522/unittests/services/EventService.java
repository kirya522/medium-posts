package tech.kirya522.unittests.services;

import tech.kirya522.unittests.entities.Event;

public interface EventService {

    Event findEvent(long id);

    void saveEvent(Event event);
}
