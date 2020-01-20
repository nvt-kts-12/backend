package nvt.kts.ticketapp.service.event.eventDay;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.event.EventDayBuyingDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.event.EventdayNotFound;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetEventDayIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventDaysRepository eventDaysRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationSchemeRepository locationSchemeRepository;

    private static Event event;
    private static EventDay eventDay1;
    private static Location location;
    private static LocationScheme locationScheme;
    private final Long NONEXISTENT_EVENTDAY_ID = 6L;

    @Before
    public void setUp() {
        locationScheme = new LocationScheme("schemeName", "address");
        location = new Location(locationScheme);
        event = new Event("name4", EventCategory.SPORT, "good4");
        eventDay1 = new EventDay(new Date(2020 - 01 - 15), location, new Date(2020 - 01 - 12), EventDayState.RESERVABLE_AND_BUYABLE, event);

        locationSchemeRepository.save(locationScheme);
        locationRepository.save(location);
        eventRepository.save(event);
        eventDaysRepository.save(eventDay1);
    }

    @Test
    public void getEventDay_Positive() throws EventdayNotFound {
        EventDayBuyingDTO eventDayBuyingDTO = eventService.getEventDay(eventDay1.getId());

        assertNotNull(eventDayBuyingDTO);
        assertEquals(eventDayBuyingDTO.getId(), eventDay1.getId());
        assertEquals(eventDayBuyingDTO.getLocationId(), eventDay1.getLocation().getId());
    }

    @Test(expected = EventdayNotFound.class)
    public void getEventDay_Negative() throws EventdayNotFound {
        eventService.getEventDay(NONEXISTENT_EVENTDAY_ID);
    }
}
