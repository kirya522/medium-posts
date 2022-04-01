package tech.kirya522.unittests.repositories;

import tech.kirya522.unittests.entities.Event;

public interface EventRepository {

    Event findEventById(long id);

    void saveEvent(Event event);
}
