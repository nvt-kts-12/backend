package nvt.kts.ticketapp.service.event.getAll;

import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.service.event.EventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetAllEventsUnitTest {

    @MockBean
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    @Before
    public void setUp(){
        List<Event> events = new ArrayList<>();
        events.add(new Event("name", EventCategory.SPORT, "good"));
        events.add(new Event("name2", EventCategory.CULTURAL, "good2"));
        Mockito.when(eventRepository.findAll()).thenReturn(events);
    }

    @Test
    public void findAllTest() {
        List<Event> events = eventService.findAllEvents();
        assertNotNull(events);
        assertThat(events).hasSize(2);
        assertThat(events.get(0).getName()).isEqualTo("name");
        assertThat(events.get(0).getCategory()).isEqualTo(EventCategory.SPORT);
        assertThat(events.get(0).getDescription()).isEqualTo("good");

    }
}
