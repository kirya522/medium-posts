package tech.kirya522.unittests.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.kirya522.unittests.dtos.event.EventDTO;
import tech.kirya522.unittests.dtos.event.EventDTOBuilder;
import tech.kirya522.unittests.entities.Event;
import tech.kirya522.unittests.services.EventService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventsControllerTest {
    private final static long ID = 1L;

    @Mock
    private EventDTOBuilder eventDTOBuilder;
    @Mock
    private EventService eventService;

    @InjectMocks
    private EventsController controller;

    @Test
    void getEvent() {
        final Event event = mock(Event.class);
        when(eventService.findEvent(ID)).thenReturn(event);
        final EventDTO eventDTO = mock(EventDTO.class);
        when(eventDTOBuilder.fromEvent(event)).thenReturn(eventDTO);

        final EventDTO actual = controller.getEvent(ID);

        assertNotNull(actual);
        assertEquals(eventDTO, actual);
        verify(eventService).findEvent(ID);
        verify(eventDTOBuilder).fromEvent(event);
    }

    @Test
    void saveEvent() {
        final Event event = mock(Event.class);
        final EventDTO eventDTO = mock(EventDTO.class);
        when(eventDTOBuilder.fromEventDTO(eventDTO)).thenReturn(event);


        controller.saveEvent(eventDTO);

        verify(eventDTOBuilder).fromEventDTO(eventDTO);
        verify(eventService).saveEvent(event);
    }
}