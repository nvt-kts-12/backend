package nvt.kts.ticketapp.service.event.update;

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
import nvt.kts.ticketapp.service.event.EventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateEventDayUnitTest {

    @MockBean
    private EventDaysRepository eventDaysRepository;

    @Autowired
    private EventService eventService;

    private static EventDay eventDay;
    private static EventDay eventDay_notFound;
    private static EventDayUpdateDTO eventDayDetails;


    @Before
    public void setUp() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        LocationScheme locationScheme = new LocationScheme("schemeName", "address");
        Location location = new Location(locationScheme);
        Event event = new Event("name4", EventCategory.SPORT, "good4");
        event.setId(1L);
        eventDay = new EventDay(format.parse("2020-01-15"), location,format.parse("2020-01-12"), EventDayState.RESERVABLE_AND_BUYABLE, event);
        eventDay.setId(10L);
        EventDay eventDay2 = new EventDay(format.parse("2020-05-25"), location,format.parse("2020-05-12"), EventDayState.RESERVABLE_AND_BUYABLE, event);
        eventDay2.setId(2L);
        eventDay_notFound = new EventDay(format.parse("2020-01-15"), location,format.parse("2020-01-12"), EventDayState.RESERVABLE_AND_BUYABLE, event);
        eventDay_notFound.setId(50L);

        Mockito.when(eventDaysRepository.save(eventDay)).thenReturn(eventDay);
        Mockito.when(eventDaysRepository.findByIdAndDeletedFalse(eventDay.getId())).thenReturn(Optional.of(eventDay));
        Mockito.when(eventDaysRepository.findByIdAndDeletedFalse(eventDay2.getId())).thenReturn(Optional.of(eventDay2));
        Mockito.when(eventDaysRepository.findByIdAndDeletedFalse(eventDay_notFound.getId())).thenReturn(Optional.empty());
        Mockito.when(eventDaysRepository.findAllByEventId(1L)).thenReturn(new ArrayList<>() {{add(eventDay); add(eventDay2);}});
    }

    @Test
    public void updateEventDay_success() throws DateFormatIsNotValid, EventdayNotFound, EventDayForDateExists {
        eventDayDetails = new EventDayUpdateDTO(10L,"2020-01-15","2020-01-12",EventDayState.NOT_IN_SALE);
        EventDayUpdateDTO updatedEventDay = eventService.updateEventDay(eventDay.getId(),eventDayDetails);
        assertNotNull(updatedEventDay);
        assertThat(updatedEventDay.getEventDayState()).isEqualTo(eventDayDetails.getEventDayState());

    }

    @Test(expected = EventdayNotFound.class)
    public void updateEventDay_EventDayNotFound() throws DateFormatIsNotValid, EventdayNotFound, EventDayForDateExists {
        eventDayDetails = new EventDayUpdateDTO(10L,"2020-01-15","2020-01-12",EventDayState.NOT_IN_SALE);
        EventDayUpdateDTO updatedEventDay = eventService.updateEventDay(eventDay_notFound.getId(),eventDayDetails);

    }

    @Test(expected = DateFormatIsNotValid.class)
    public void updateEventDay_DateFormatNotValid() throws DateFormatIsNotValid, EventdayNotFound, EventDayForDateExists {
        eventDayDetails = new EventDayUpdateDTO(10L,"15-1-2020","12-1-2020",EventDayState.NOT_IN_SALE);
        EventDayUpdateDTO updatedEventDay = eventService.updateEventDay(eventDay.getId(),eventDayDetails);
    }

    @Test(expected = EventDayForDateExists.class)
    public void updateEventDay_EventDayForDateExists() throws DateFormatIsNotValid, EventdayNotFound, EventDayForDateExists {
        eventDayDetails = new EventDayUpdateDTO(10L,"2020-05-25","2020-05-12",EventDayState.NOT_IN_SALE);
        EventDayUpdateDTO updatedEventDay = eventService.updateEventDay(eventDay.getId(),eventDayDetails);
    }

}
