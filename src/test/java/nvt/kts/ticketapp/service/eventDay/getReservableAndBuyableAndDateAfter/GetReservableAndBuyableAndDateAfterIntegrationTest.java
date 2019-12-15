package nvt.kts.ticketapp.service.eventDay.getReservableAndBuyableAndDateAfter;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExist;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExistOrStateIsNotValid;
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

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetReservableAndBuyableAndDateAfterIntegrationTest {

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
    public void setUp() throws ParseException {

        String date1="2020-05-05";
        String date2="2020-05-03";
        Date eventDayDate=new SimpleDateFormat("yyyy-MM-dd").parse(date1);
        Date expireDate=new SimpleDateFormat("yyyy-MM-dd").parse(date2);

        locationScheme = new LocationScheme("schemeName", "address");
        location = new Location(locationScheme);
        event = new Event("name4", EventCategory.SPORT, "good4");
        eventDay1 = new EventDay(new Date(2020-05-05), location,new Date(2020-05-03), EventDayState.RESERVABLE_AND_BUYABLE, event);
        eventDay2 = new EventDay(eventDayDate, location,expireDate, EventDayState.NOT_IN_SALE, event);

        locationSchemeRepository.save(locationScheme);
        locationRepository.save(location);
        eventRepository.save(event);
        eventDaysRepository.save(eventDay1);
        eventDaysRepository.save(eventDay2);
    }

/*
    @Test
    public void GetReservableAndBuyableAndDateAfter_success() throws EventDayDoesNotExist, EventDayDoesNotExistOrStateIsNotValid {
        EventDay eventDay_found = eventDayService.getReservableAndBuyableAndDateAfter(eventDay1.getId(),eventDay1.getDate());
        assertNotNull(eventDay_found);
        assertEquals(eventDay_found.getState(),eventDay1.getState());
    }
*/
    @Test(expected = EventDayDoesNotExistOrStateIsNotValid.class)
    public void GetReservableAndBuyableAndDateAfter_EventDayDoesNotExist() throws EventDayDoesNotExist, EventDayDoesNotExistOrStateIsNotValid {
        EventDay eventDay_found = eventDayService.getReservableAndBuyableAndDateAfter(501L,eventDay1.getDate());
    }

    @Test(expected = EventDayDoesNotExistOrStateIsNotValid.class)
    public void GetReservableAndBuyableAndDateBefore_EventDayStateNotValid() throws EventDayDoesNotExist, EventDayDoesNotExistOrStateIsNotValid {
        EventDay eventDay_found = eventDayService.getReservableAndBuyableAndDateAfter(eventDay2.getId(),eventDay2.getDate());
    }
}
