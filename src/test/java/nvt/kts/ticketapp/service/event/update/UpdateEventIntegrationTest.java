package nvt.kts.ticketapp.service.event.update;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.service.event.EventService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateEventIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    private EventDTO eventDetails;
    private EventDTO eventForChange;
    private Event event;
    private Event event_not_in_database;

    @Before
    public void setUp(){
        event = new Event("Koncert Zdravka Colica",EventCategory.ENTERTAINMENT,"opis");
        eventDetails = new EventDTO(null, "opis", "Koncert Zdravka Colica", EventCategory.ENTERTAINMENT);
        eventRepository.save(event);

        event_not_in_database =new Event ("Koncert Zdravka Colica2",EventCategory.ENTERTAINMENT,"opis2");

    }

    @Test
    public void updateEvent_success() throws EventNotFound {
        EventDTO updatedEvent = eventService.update(event.getId(),eventDetails);
        assertNotNull(updatedEvent);
        assertThat(updatedEvent.getDescription()).isEqualTo(eventDetails.getDescription());
        assertThat(updatedEvent.getName()).isEqualTo(eventDetails.getName());
        assertThat((updatedEvent.getCategory())).isEqualTo(eventDetails.getCategory());
    }

    @Test(expected = EventNotFound.class)
    public void updateEvent_EventNotFound() throws EventNotFound {
        EventDTO updatedEvent = eventService.update(event_not_in_database.getId(),eventDetails);
    }

}
