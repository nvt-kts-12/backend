package nvt.kts.ticketapp.service.event.searchAndFilter;

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
import nvt.kts.ticketapp.service.event.EventService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchAndFilterIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventDaysRepository eventDaysRepository;

    @Autowired
    private LocationSchemeRepository locationSchemeRepository;

    @Autowired
    private LocationRepository locationRepository;

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

        Pageable pageable = PageRequest.of(0, 10);

        EventsDTO eventsDTO = eventService.findAll(pageable, null, null,
                null, null);

        assertNotNull(eventsDTO);
        assertEquals(eventsDTO.getNumberOfElements(), 2);

        List<Event> events = eventsDTO.getEvents();
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

        Pageable pageable = PageRequest.of(0, 10);

        EventsDTO eventsDTO = eventService.findAll(pageable, "name", null,
                null, null);

        assertNotNull(eventsDTO);
        assertEquals(eventsDTO.getNumberOfElements(), 2);

        List<Event> events = eventsDTO.getEvents();
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

        Pageable pageable = PageRequest.of(0, 10);

        EventsDTO eventsDTO = eventService.findAll(pageable, null, "2020-12-10",
                null, null);

        assertNotNull(eventsDTO);
        assertEquals(eventsDTO.getNumberOfElements(), 1);

        List<Event> events = eventsDTO.getEvents();
        assertEquals(events.size(), 1);

        assertEquals(events.get(0).getName(), "name");
        assertEquals(events.get(0).getCategory(), EventCategory.SPORT);
        assertEquals(events.get(0).getDescription(), "good");

    }

    @Test
    public void searchAndFilter_typeFilter() {

        Pageable pageable = PageRequest.of(0, 10);

        EventsDTO eventsDTO = eventService.findAll(pageable, null, null,
                "CULTURAL", null);

        assertNotNull(eventsDTO);
        assertEquals(eventsDTO.getNumberOfElements(), 1);

        List<Event> events = eventsDTO.getEvents();
        assertEquals(events.size(), 1);

        assertEquals(events.get(0).getName(), "name2");
        assertEquals(events.get(0).getCategory(), EventCategory.CULTURAL);
        assertEquals(events.get(0).getDescription(), "good2");

    }

    @Test
    public void searchAndFilter_locationFilter() {

        Pageable pageable = PageRequest.of(0, 10);

        EventsDTO eventsDTO = eventService.findAll(pageable, null, null,
                null, "Spens");

        assertNotNull(eventsDTO);
        assertEquals(eventsDTO.getNumberOfElements(), 1);

        List<Event> events = eventsDTO.getEvents();
        assertEquals(events.size(), 1);

        assertEquals(events.get(0).getName(), "name");
        assertEquals(events.get(0).getCategory(), EventCategory.SPORT);
        assertEquals(events.get(0).getDescription(), "good");

    }

    @Test
    public void searchAndFilter_allParameters_noResults() {

        Pageable pageable = PageRequest.of(0, 10);

        EventsDTO eventsDTO = eventService.findAll(pageable, "name2", "2020-12-10",
                "CULTURAL", "Spens");

        assertNotNull(eventsDTO);
        assertEquals(eventsDTO.getNumberOfElements(), 0);

        List<Event> events = eventsDTO.getEvents();
        assertEquals(events.size(), 0);

    }

    @Test
    public void searchAndFilter_allParameters_withResults() {

        Pageable pageable = PageRequest.of(0, 10);

        EventsDTO eventsDTO = eventService.findAll(pageable, "name", "2020-12-10",
                "SPORT", "Spens");

        assertNotNull(eventsDTO);
        assertEquals(eventsDTO.getNumberOfElements(), 1);

        List<Event> events = eventsDTO.getEvents();
        assertEquals(events.size(), 1);

        assertEquals(events.get(0).getName(), "name");
        assertEquals(events.get(0).getCategory(), EventCategory.SPORT);
        assertEquals(events.get(0).getDescription(), "good");

    }

}
