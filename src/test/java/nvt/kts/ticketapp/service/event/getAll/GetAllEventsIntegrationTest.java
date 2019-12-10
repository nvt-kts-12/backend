package nvt.kts.ticketapp.service.event.getAll;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.service.event.EventService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetAllEventsIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Before
    public void setUp(){
        Event event1 = new  Event("name", EventCategory.SPORT, "good");
        Event event2 = new Event("name2", EventCategory.CULTURAL, "good2");

        eventRepository.save(event1);
        eventRepository.save(event2);

    }

    @Test
    public void getAll(){
        List<Event> events = eventService.findAllEvents();
        assertNotNull(events);
        assertThat(events).hasSize(2);
        assertThat(events.get(0).getName()).isEqualTo("name");
        assertThat(events.get(0).getCategory()).isEqualTo(EventCategory.SPORT);
        assertThat(events.get(0).getDescription()).isEqualTo("good");
    }
}
