package nvt.kts.ticketapp.service.eventDay.getByIdAndDateAfter;

import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.event.EventdayNotFound;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.service.event.EventDayService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetByIdAndDateAfterUnitTest {

    private final Long EXISTING_EVENTDAY_ID = 1L;
    private final Long NONEXISTENT_EVENTDAY_ID = 5L;

    @MockBean
    private EventDaysRepository eventDaysRepository;
    @Autowired
    private EventDayService eventDayService;

    private Event event;
    private EventDay eventDay;
    private Location location;
    private LocationScheme locationScheme;
    private Date eventDayDate;
    private Date expireDate;

    @Before
    public void setUp() throws ParseException {
        eventDayDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-05");
        expireDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-01");

        locationScheme = new LocationScheme("Scheme 1", "Scheme address 1");
        location = new Location(locationScheme);
        event = new Event("Event 1", EventCategory.SPORT, "Event description.");
        eventDay = new EventDay(eventDayDate, location, expireDate, EventDayState.RESERVABLE_AND_BUYABLE, event);
        eventDay.setId(EXISTING_EVENTDAY_ID);

        Mockito.when(eventDaysRepository.findOneByIdAndDateAfter(EXISTING_EVENTDAY_ID, eventDayDate))
                .thenReturn(Optional.of(eventDay));

        Mockito.when(eventDaysRepository.findOneByIdAndDateAfter(NONEXISTENT_EVENTDAY_ID, eventDayDate))
                .thenReturn(Optional.empty());
    }

    @Test
    public void getByIdAndDateAfter_Positive() throws EventdayNotFound {
        EventDay foundEventDay = eventDayService.getByIdAndDateAfter(EXISTING_EVENTDAY_ID, eventDay.getDate());

        assertNotNull(foundEventDay);
        assertEquals(EXISTING_EVENTDAY_ID, foundEventDay.getId());
        assertEquals(location.getScheme().getName(), foundEventDay.getLocation().getScheme().getName());
        assertEquals(event.getName(), foundEventDay.getEvent().getName());
    }

    @Test(expected = EventdayNotFound.class)
    public void getByIdAndDateAfter_Negative_NonexistentId() throws EventdayNotFound {
        eventDayService.getByIdAndDateAfter(NONEXISTENT_EVENTDAY_ID, eventDay.getDate());
    }
}
