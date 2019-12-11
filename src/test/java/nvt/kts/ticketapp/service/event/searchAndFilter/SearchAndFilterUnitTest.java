package nvt.kts.ticketapp.service.event.searchAndFilter;

import nvt.kts.ticketapp.domain.dto.event.EventsDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchAndFilterUnitTest {

    @MockBean
    private EventRepository eventRepositoryMocked;

    @Autowired
    private EventService eventService;

    @Before
    public void setUp() {

        Event event1 = new Event("name", EventCategory.SPORT, "good");
        Event event2 = new Event("name2", EventCategory.CULTURAL, "good2");

        Mockito.when(eventRepositoryMocked.findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<Event>(){{
                add(event1);
                add(event2);
            }}));

        Mockito.when(eventRepositoryMocked.executeCustomQuery(Mockito.any(Pageable.class), Mockito.eq("name"),
                Mockito.isNull(),Mockito.isNull(), Mockito.isNull())).thenReturn(new PageImpl<>(new ArrayList<Event>(){{
            add(event1);
            add(event2);
        }}));

        Mockito.when(eventRepositoryMocked.executeCustomQuery(Mockito.any(Pageable.class), Mockito.isNull(),
                Mockito.eq("2019-12-10"),Mockito.isNull(), Mockito.isNull())).thenReturn(new PageImpl<>(new ArrayList<Event>(){{
            add(event1);
        }}));

        Mockito.when(eventRepositoryMocked.executeCustomQuery(Mockito.any(Pageable.class), Mockito.isNull(),
                Mockito.isNull(),Mockito.eq("CULTURAL"), Mockito.isNull())).thenReturn(new PageImpl<>(new ArrayList<Event>(){{
            add(event2);
        }}));

        Mockito.when(eventRepositoryMocked.executeCustomQuery(Mockito.any(Pageable.class), Mockito.isNull(),
                Mockito.isNull(),Mockito.isNull(), Mockito.eq("Spens"))).thenReturn(new PageImpl<>(new ArrayList<Event>(){{
            add(event1);
        }}));

        Mockito.when(eventRepositoryMocked.executeCustomQuery(Mockito.any(Pageable.class), Mockito.eq("name"),
                Mockito.eq("2019-12-10"),Mockito.eq("CULTURAL")
                , Mockito.eq("Spens"))).thenReturn(new PageImpl<>(new ArrayList<Event>()));
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

        EventsDTO eventsDTO = eventService.findAll(pageable, null, "2019-12-10",
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
    public void searchAndFilter_allParameters() {

        Pageable pageable = PageRequest.of(0, 10);

        EventsDTO eventsDTO = eventService.findAll(pageable, "name", "2019-12-10",
                "CULTURAL", "Spens");

        assertNotNull(eventsDTO);
        assertEquals(eventsDTO.getNumberOfElements(), 0);

        List<Event> events = eventsDTO.getEvents();
        assertEquals(events.size(), 0);
    }

}
