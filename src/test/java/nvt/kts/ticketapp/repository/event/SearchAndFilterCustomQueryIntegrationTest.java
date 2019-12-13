package nvt.kts.ticketapp.repository.event;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchAndFilterCustomQueryIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private EventRepository eventRepository;

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
    public void searchAndFilter_searchQuery() {

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);

        Page<Event> eventsPage = eventRepository.executeCustomQuery(pageable, "name", null, null, null);

        assertNotNull(eventsPage);
        assertEquals(eventsPage.getTotalElements(), 2);

        List<Event> events  = eventsPage.getContent();
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
        Pageable pageable = PageRequest.of(page,size);

        Page<Event> eventsPage = eventRepository.executeCustomQuery(pageable, null, "2020-12-10",
                null, null);

        assertNotNull(eventsPage);
        assertEquals(eventsPage.getTotalElements(), 1);

        List<Event> events  = eventsPage.getContent();
        assertEquals(events.size(), 1);

        assertEquals(events.get(0).getName(), "name");
        assertEquals(events.get(0).getCategory(), EventCategory.SPORT);
        assertEquals(events.get(0).getDescription(), "good");

    }

    @Test
    public void searchAndFilter_typeFilter() {

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);

        Page<Event> eventsPage = eventRepository.executeCustomQuery(pageable, null, null,
                "CULTURAL", null);

        assertNotNull(eventsPage);
        assertEquals(eventsPage.getTotalElements(), 1);

        List<Event> events  = eventsPage.getContent();
        assertEquals(events.size(), 1);

        assertEquals(events.get(0).getName(), "name2");
        assertEquals(events.get(0).getCategory(), EventCategory.CULTURAL);
        assertEquals(events.get(0).getDescription(), "good2");

    }

    @Test
    public void searchAndFilter_locationFilter() {

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);

        Page<Event> eventsPage = eventRepository.executeCustomQuery(pageable, null, null,
                null, "Spens");

        assertNotNull(eventsPage);
        assertEquals(eventsPage.getTotalElements(), 1);

        List<Event> events  = eventsPage.getContent();
        assertEquals(events.size(), 1);

        assertEquals(events.get(0).getName(), "name");
        assertEquals(events.get(0).getCategory(), EventCategory.SPORT);
        assertEquals(events.get(0).getDescription(), "good");

    }

    @Test
    public void searchAndFilter_dateFilter_removeSingleQuotationMarks() {

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);

        Page<Event> eventsPage = eventRepository.executeCustomQuery(pageable, null, "''2020-12-10''''''''",
                null, null);

        assertNotNull(eventsPage);
        assertEquals(eventsPage.getTotalElements(), 1);

        List<Event> events  = eventsPage.getContent();
        assertEquals(events.size(), 1);

        assertEquals(events.get(0).getName(), "name");
        assertEquals(events.get(0).getCategory(), EventCategory.SPORT);
        assertEquals(events.get(0).getDescription(), "good");

    }

    @Test
    public void searchAndFilter_allParameters_noResults() {

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);

        Page<Event> eventsPage = eventRepository.executeCustomQuery(pageable, "name2", "2020-12-10",
                "CULTURAL", "Spens");

        assertNotNull(eventsPage);
        assertEquals(eventsPage.getTotalElements(), 0);

        List<Event> events  = eventsPage.getContent();
        assertEquals(events.size(), 0);

    }

    @Test
    public void searchAndFilter_allParameters_withResults() {

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);

        Page<Event> eventsPage = eventRepository.executeCustomQuery(pageable, "name", "2020-12-10",
                "SPORT", "Spens");

        assertNotNull(eventsPage);
        assertEquals(eventsPage.getTotalElements(), 1);

        List<Event> events  = eventsPage.getContent();
        assertEquals(events.size(), 1);

        assertEquals(events.get(0).getName(), "name");
        assertEquals(events.get(0).getCategory(), EventCategory.SPORT);
        assertEquals(events.get(0).getDescription(), "good");

    }

}
