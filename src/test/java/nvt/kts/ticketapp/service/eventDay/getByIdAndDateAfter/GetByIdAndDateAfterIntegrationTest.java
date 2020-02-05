package nvt.kts.ticketapp.service.eventDay.getByIdAndDateAfter;

import nvt.kts.ticketapp.ClearDatabaseRule;
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
import nvt.kts.ticketapp.service.event.EventDayService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetByIdAndDateAfterIntegrationTest {

    private final Long NONEXISTENT_EVENTDAY_ID = 5L;


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
    private static Location location;
    private static LocationScheme locationScheme;
    private Date eventDayDate;
    private Date expireDate;

    @Before
    public void setUp() throws ParseException {

        eventDayDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-05");
        expireDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-01");

        locationScheme = locationSchemeRepository.save(new LocationScheme("schemeName", "address"));
        location = locationRepository.save(new Location(locationScheme));
        event = eventRepository.save(new Event("name4", EventCategory.SPORT, "good4"));
        eventDay1 = eventDaysRepository.save(new EventDay(eventDayDate, location, expireDate, EventDayState.RESERVABLE_AND_BUYABLE, event));
    }

    /**
     * FIXME: FAILS FOR NO REASON. Kao ne postoji event sa eventday1.getId() u bazi
     * @throws EventdayNotFound
     */
    @Test
    public void getByIdAndDateAfter_Positive() throws EventdayNotFound {
        EventDay foundEventDay = eventDayService.getByIdAndDateAfter(eventDay1.getId(), eventDay1.getDate());

        assertNotNull(foundEventDay);
        assertEquals(eventDay1.getId(), foundEventDay.getId());
        assertEquals(location.getId(), foundEventDay.getLocation().getId());
        assertEquals(locationScheme.getId(), foundEventDay.getLocation().getScheme().getId());
        assertEquals(event.getId(), foundEventDay.getEvent().getId());
    }

    @Test(expected = EventdayNotFound.class)
    public void getByIdAndDateAfter_Negative_NonexistentId() throws EventdayNotFound {
        eventDayService.getByIdAndDateAfter(NONEXISTENT_EVENTDAY_ID, eventDay1.getDate());
    }


}
