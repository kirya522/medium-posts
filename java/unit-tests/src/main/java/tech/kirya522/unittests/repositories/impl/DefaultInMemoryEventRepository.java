package tech.kirya522.unittests.repositories.impl;

import org.springframework.stereotype.Repository;
import tech.kirya522.unittests.entities.Event;
import tech.kirya522.unittests.repositories.EventRepository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DefaultInMemoryEventRepository implements EventRepository {

    private final ConcurrentHashMap<Long, Event> storage;

    public DefaultInMemoryEventRepository() {
        storage = new ConcurrentHashMap<>();
    }

    @Override
    public Event findEventById(long id) {
        return storage.get(id);
    }

    @Override
    public void saveEvent(Event event) {
        storage.put(event.getId(), event);
    }
}
