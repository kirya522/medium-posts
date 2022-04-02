package tech.kirya522.unittests.repositories.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.kirya522.unittests.entities.Event;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultInMemoryEventRepositoryTest {
    private static final long ID = 1L;

    private DefaultInMemoryEventRepository repository;

    @BeforeEach
    void setUp() {
        repository = new DefaultInMemoryEventRepository();
    }

    @Test
    public void findEventById_shouldFindEvent_whenExists() {
        final Event event = mock(Event.class);
        when(event.getId()).thenReturn(ID);

        repository.saveEvent(event);
        final Event actual = repository.findEventById(ID);

        assertNotNull(actual);
        assertEquals(event, actual);
    }

    @Test
    public void saveEvent_shouldSaveLastCalledEvent_whenCalledMultipleTimes() {
        final Event event = mock(Event.class);
        when(event.getId()).thenReturn(ID);
        final Event lastEvent = mock(Event.class);
        when(lastEvent.getId()).thenReturn(ID);

        repository.saveEvent(event);
        repository.saveEvent(lastEvent);
        final Event actual = repository.findEventById(ID);

        assertNotNull(actual);
        assertEquals(lastEvent, actual);
    }
}