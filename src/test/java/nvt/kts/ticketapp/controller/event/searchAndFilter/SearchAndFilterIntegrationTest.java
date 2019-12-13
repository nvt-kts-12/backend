package nvt.kts.ticketapp.controller.event.searchAndFilter;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.event.EventsDTO;
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
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class SearchAndFilterIntegrationTest {

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

    private final String URL = "/api/event/show-events";

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

        EventDay eventDay = new EventDay(sdf.parse("2020-12-10"), location,
                sdf.parse("2020-12-05"), EventDayState.RESERVABLE_AND_BUYABLE, event1);
        eventDaysRepository.save(eventDay);
    }


    @Test
    public void searchAndFilter_noParameters() {

        int page = 0;
        int size = 10;

        String url = URL + "?page=" + page + "&size=" + size;

        ResponseEntity<EventsDTO> response = restTemplate.getForEntity(url, EventsDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        List<Event> events  = response.getBody().getEvents();

        assertEquals(events.size(), 2);

        assertEquals(events.get(0).getName(), "name");
        assertEquals(events.get(0).getCategory(), EventCategory.SPORT);
        assertEquals(events.get(0).getDescription(), "good");
        assertEquals(events.get(1).getName(), "name2");
        assertEquals(events.get(1).getCategory(), EventCategory.CULTURAL);
        assertEquals(events.get(1).getDescription(), "good2");

    }

    @Test
    public void searchAndFilter_searchQuery() {

        int page = 0;
        int size = 10;
        String searchQuery = "name";

        String url = URL + "?page=" + page + "&size=" + size + "&searchQuery=" + searchQuery;


        ResponseEntity<EventsDTO> response = restTemplate.getForEntity(url, EventsDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(response.getBody().getNumberOfElements(), 2);

        List<Event> events  = response.getBody().getEvents();
        assertEquals(events.size(), 2);

        assertEquals(events.get(0).getName(), "name");
        assertEquals(events.get(0).getCategory(), EventCategory.SPORT);
        assertEquals(events.get(0).getDescription(), "good");
        assertEquals(events.get(1).getName(), "name2");
        assertEquals(events.get(1).getCategory(), EventCategory.CULTURAL);
        assertEquals(events.get(1).getDescription(), "good2");

    }

    @Test
    public void searchAndFilter_dateFilter() {

        int page = 0;
        int size = 10;
        String dateFilter = "2020-12-10";

        String url = URL + "?page=" + page + "&size=" + size + "&dateFilter=" + dateFilter;


        ResponseEntity<EventsDTO> response = restTemplate.getForEntity(url, EventsDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(response.getBody().getNumberOfElements(), 1);

        List<Event> events  = response.getBody().getEvents();
        assertEquals(events.size(), 1);


        assertEquals(events.get(0).getName(), "name");
        assertEquals(events.get(0).getCategory(), EventCategory.SPORT);
        assertEquals(events.get(0).getDescription(), "good");

    }

    @Test
    public void searchAndFilter_typeFilter() {

        int page = 0;
        int size = 10;
        String typeFilter = "CULTURAL";

        String url = URL + "?page=" + page + "&size=" + size + "&typeFilter=" + typeFilter;

        ResponseEntity<EventsDTO> response = restTemplate.getForEntity(url, EventsDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(response.getBody().getNumberOfElements(), 1);

        List<Event> events  = response.getBody().getEvents();
        assertEquals(events.size(), 1);

        assertEquals(events.get(0).getName(), "name2");
        assertEquals(events.get(0).getCategory(), EventCategory.CULTURAL);
        assertEquals(events.get(0).getDescription(), "good2");

    }

    @Test
    public void searchAndFilter_locationFilter() {

        int page = 0;
        int size = 10;
        String locationFilter = "Spens";

        String url = URL + "?page=" + page + "&size=" + size + "&locationFilter=" + locationFilter;

        ResponseEntity<EventsDTO> response = restTemplate.getForEntity(url, EventsDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(response.getBody().getNumberOfElements(), 1);

        List<Event> events  = response.getBody().getEvents();
        assertEquals(events.size(), 1);

        assertEquals(events.get(0).getName(), "name");
        assertEquals(events.get(0).getCategory(), EventCategory.SPORT);
        assertEquals(events.get(0).getDescription(), "good");

    }

    @Test
    public void searchAndFilter_allParameters_noResults() {

        int page = 0;
        int size = 10;
        String searchQuery = "name2";
        String dateFilter = "2020-12-10";
        String typeFilter = "CULTURAL";
        String locationFilter = "Spens";

        String url = URL + "?page=" + page + "&size=" + size + "&searchQuery=" + searchQuery +
                "&dateFilter=" + dateFilter + "&typeFilter=" + typeFilter + "&locationFilter=" + locationFilter;

        ResponseEntity<EventsDTO> response = restTemplate.getForEntity(url, EventsDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(response.getBody().getNumberOfElements(), 0);

        List<Event> events  = response.getBody().getEvents();
        assertEquals(events.size(), 0);

    }

    @Test
    public void searchAndFilter_allParameters_withResults() {

        int page = 0;
        int size = 10;
        String searchQuery = "name";
        String dateFilter = "2020-12-10";
        String typeFilter = "SPORT";
        String locationFilter = "Spens";

        String url = URL + "?page=" + page + "&size=" + size + "&searchQuery=" + searchQuery +
                "&dateFilter=" + dateFilter + "&typeFilter=" + typeFilter + "&locationFilter=" + locationFilter;

        ResponseEntity<EventsDTO> response = restTemplate.getForEntity(url, EventsDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(response.getBody().getNumberOfElements(), 1);

        List<Event> events  = response.getBody().getEvents();
        assertEquals(events.size(), 1);

        assertEquals(events.get(0).getName(), "name");
        assertEquals(events.get(0).getCategory(), EventCategory.SPORT);
        assertEquals(events.get(0).getDescription(), "good");

    }
}
