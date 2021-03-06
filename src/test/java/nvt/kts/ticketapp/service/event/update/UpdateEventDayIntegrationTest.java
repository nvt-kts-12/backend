package nvt.kts.ticketapp.service.event.update;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.event.EventDayDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayUpdateDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.date.DateFormatIsNotValid;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExist;
import nvt.kts.ticketapp.exception.event.EventDayForDateExists;
import nvt.kts.ticketapp.exception.event.EventdayNotFound;
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
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateEventDayIntegrationTest {

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

    private static EventDay eventDay1;
    private static EventDayUpdateDTO eventDayDetails;
    private static EventDayUpdateDTO eventDayDetails_badDateFormat;
    private static  EventDayUpdateDTO eventDayDetails_dateExists;

    @Before
    public void setUp() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        LocationScheme locationScheme = new LocationScheme("schemeName", "address");
        Location location = new Location(locationScheme);
        Event event = new Event("name4", EventCategory.SPORT, "good4");
        eventDay1 = new EventDay(format.parse("2020-01-28"), location,format.parse("2020-01-27"), EventDayState.RESERVABLE_AND_BUYABLE, event);
        EventDay eventDay2 = new EventDay(format.parse("2020-05-25"), location, format.parse("2020-05-23"), EventDayState.RESERVABLE_AND_BUYABLE, event);
        eventDayDetails = new EventDayUpdateDTO(1L,"2020-01-12","2020-01-10",EventDayState.SOLD_OUT);
        eventDayDetails_badDateFormat = new EventDayUpdateDTO(1L,"12-01-2020","10-01-2020",EventDayState.SOLD_OUT);
        eventDayDetails_dateExists = new EventDayUpdateDTO(1L,"2020-05-25","2020-05-12",EventDayState.SOLD_OUT);
        locationSchemeRepository.save(locationScheme);
        locationRepository.save(location);
        eventRepository.save(event);
        eventDaysRepository.save(eventDay1);
        eventDaysRepository.save(eventDay2);
    }

    @Test
    public void update_success() throws DateFormatIsNotValid, EventdayNotFound, EventDayForDateExists {
        EventDayUpdateDTO updated = eventService.updateEventDay(eventDay1.getId(),eventDayDetails);
        assertNotNull(updated);
        assertEquals(updated.getEventDayState(),eventDayDetails.getEventDayState());
    }

    @Test(expected = DateFormatIsNotValid.class)
    public void update_DateFormatIsNotValid() throws DateFormatIsNotValid, EventdayNotFound, EventDayForDateExists {
        EventDayUpdateDTO updated = eventService.updateEventDay(eventDay1.getId(),eventDayDetails_badDateFormat);
    }

    @Test(expected = EventdayNotFound.class)
    public void update_EventdayNotFound() throws DateFormatIsNotValid, EventdayNotFound, EventDayForDateExists {
        EventDayUpdateDTO updated = eventService.updateEventDay(500L,eventDayDetails);
    }

    @Test(expected = EventDayForDateExists.class)
    public void update_EventDayForDateExists() throws DateFormatIsNotValid, EventdayNotFound, EventDayForDateExists {
        EventDayUpdateDTO updated = eventService.updateEventDay(eventDay1.getId(), eventDayDetails_dateExists);
    }

}
