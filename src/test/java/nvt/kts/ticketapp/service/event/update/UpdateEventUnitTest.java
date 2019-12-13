package nvt.kts.ticketapp.service.event.update;

import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.service.event.EventService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateEventUnitTest {

    @MockBean
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    private static Event eventForUpdate;
    private static Event event_notFound;
    private static EventDTO eventDetails;

    @Before
    public void setUp(){
        event_notFound = new Event("name",EventCategory.SPORT,"good");

        eventForUpdate= new Event("name4", EventCategory.SPORT, "good4");
        eventForUpdate.setId(6L);
        eventDetails = new EventDTO(2L, "updatedDescription", "updatedName", EventCategory.SPORT);

        Mockito.when(eventRepository.save(eventForUpdate)).thenReturn(eventForUpdate);
        Mockito.when(eventRepository.findByIdAndDeletedFalse(eventForUpdate.getId())).thenReturn(Optional.of(eventForUpdate));
    }

    @Test(expected = EventNotFound.class)
    public void updateEvent_eventNotFound() throws EventNotFound {
        Mockito.when(eventRepository.findByIdAndDeletedFalse(event_notFound.getId())).thenReturn(Optional.empty());
        eventService.update(event_notFound.getId(),eventDetails);
    }

    @Test
    public void updateEvent_success() throws EventNotFound {

        EventDTO updatedEvent = eventService.update(eventForUpdate.getId(), eventDetails);
        assertNotNull(updatedEvent);
        assertThat(updatedEvent.getDescription()).isEqualTo(eventDetails.getDescription());
        assertThat(updatedEvent.getName()).isEqualTo(eventDetails.getName());
        assertThat((updatedEvent.getCategory())).isEqualTo(eventDetails.getCategory());

        Mockito.verify(eventRepository, Mockito.times(1)).findByIdAndDeletedFalse(eventForUpdate.getId());

    }
}
