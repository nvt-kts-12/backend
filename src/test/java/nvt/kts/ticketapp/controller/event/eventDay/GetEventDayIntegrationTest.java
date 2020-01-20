package nvt.kts.ticketapp.controller.event.eventDay;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.event.EventDayBuyingDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class GetEventDayIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventDaysRepository eventDaysRepository;

    @Autowired
    private LocationSchemeRepository locationSchemeRepository;

    @Autowired
    private LocationRepository locationRepository;

    @LocalServerPort
    private int port;


    private final String URL = "/api/event/eventDay/";
    private final Long NONEXISTENT_EVENTDAY_ID = 6L;

    private EventDay eventDay;

    @Before
    public void setUp() throws ParseException {
        Event event1 = new Event("name", EventCategory.SPORT, "good");
        Event event2 = new Event("name2", EventCategory.CULTURAL, "good2");

        eventRepository.save(event1);
        eventRepository.save(event2);

        LocationScheme locationScheme = new LocationScheme("Spens", "Novi Sad");
        locationSchemeRepository.save(locationScheme);

        Location location = new Location(locationScheme);
        locationRepository.save(location);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        eventDay = new EventDay(sdf.parse("2020-12-10"), location,
                sdf.parse("2020-12-05"), EventDayState.RESERVABLE_AND_BUYABLE, event1);
        eventDaysRepository.save(eventDay);
    }

    @Test
    public void getEventDay_Positive(){
        String url = URL + eventDay.getId();
        ResponseEntity<EventDayBuyingDTO> response = restTemplate.getForEntity(url, EventDayBuyingDTO.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().getId(), eventDay.getId());
        assertEquals(response.getBody().getLocationId(), eventDay.getLocation().getId());
    }

    @Test
    public void getEventDay_Negative(){
        String url = URL + NONEXISTENT_EVENTDAY_ID;

        ResponseEntity<EventDayBuyingDTO> response = restTemplate.getForEntity(url, EventDayBuyingDTO.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
