package tech.kirya522.unittests.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.kirya522.unittests.entities.Event;
import tech.kirya522.unittests.repositories.EventRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultEventServiceTest {
    private static final long ID = 1L;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private DefaultEventService service;

    @Test
    void findEvent_shouldCallRepository() {
        final Event event = mock(Event.class);
        when(eventRepository.findEventById(ID)).thenReturn(event);

        final Event actual = service.findEvent(ID);

        assertNotNull(actual);
        assertEquals(event, actual);
        verify(eventRepository).findEventById(ID);
    }

    @Test
    void saveEvent_shouldCallRepository() {
        final Event event = mock(Event.class);

        service.saveEvent(event);

        verify(eventRepository).saveEvent(event);
    }
}