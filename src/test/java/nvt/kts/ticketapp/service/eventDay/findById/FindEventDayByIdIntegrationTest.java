package nvt.kts.ticketapp.service.eventDay.findById;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExist;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.service.event.EventDayService;
import nvt.kts.ticketapp.service.event.EventService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FindEventDayByIdIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private EventDayService eventDayService;

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
    private static EventDay eventDay2;
    private static Location location;
    private static LocationScheme locationScheme;

    @Before
    public void setUp(){

        locationScheme = new LocationScheme("schemeName", "address");
        location = new Location(locationScheme);
        event = new Event("name4", EventCategory.SPORT, "good4");
        eventDay1 = new EventDay(new Date(2020 - 01 - 15), location, new Date(2020 - 01 - 12), EventDayState.RESERVABLE_AND_BUYABLE, event);
        eventDay2 = new EventDay(new Date(2020 - 01 - 12), location, new Date(2020 - 01 - 10), EventDayState.NOT_IN_SALE, event);

        locationSchemeRepository.save(locationScheme);
        locationRepository.save(location);
        eventRepository.save(event);
        eventDaysRepository.save(eventDay1);
       // eventDaysRepository.save(eventDay2);
    }

    @Test
    public void findById_success() throws EventDayDoesNotExist {
        EventDay eventDay_found = eventDayService.findOneById(eventDay1.getId());
        assertNotNull(eventDay_found);
        assertEquals(eventDay_found.getState(), eventDay1.getState());
    }

    @Test(expected = EventDayDoesNotExist.class)
    public void findById_EventDayDoesNotExist() throws EventDayDoesNotExist {
        EventDay eventDay = eventDayService.findOneById(60L);
    }
}
