package nvt.kts.ticketapp.service.eventDay.getReservableAndBuyableAndDateAfter;

import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExist;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExistOrStateIsNotValid;
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

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetReservableAndBuyableAndDateAfterUnitTest {

    @MockBean
    private EventDaysRepository eventDaysRepository;

    @Autowired
    private EventDayService eventDayService;

    private static Event event;
    private static EventDay eventDay1;
    private static EventDay eventDay2;
    private static Location location;
    private static LocationScheme locationScheme;

    @Before
    public void setUp() {
        locationScheme = new LocationScheme("schemeName", "address");
        location = new Location(locationScheme);
        event = new Event("name4", EventCategory.SPORT, "good4");
        eventDay1 = new EventDay(new Date(2020 - 01 - 15), location, new Date(2020 - 01 - 12), EventDayState.RESERVABLE_AND_BUYABLE, event);
        eventDay2 = new EventDay(new Date(2020 - 01 - 12), location, new Date(2020 - 01 - 10), EventDayState.NOT_IN_SALE, event);


        Mockito.when(eventDaysRepository.findOneByIdAndStateAndDateAfter(eventDay1.getId(), eventDay1.getState(),eventDay1.getDate())).thenReturn(java.util.Optional.ofNullable(eventDay1));

    }

    @Test
    public void getReservableAndBuyableAndDateAfter_success() throws EventDayDoesNotExist, EventDayDoesNotExistOrStateIsNotValid {

        EventDay found = eventDayService.getReservableAndBuyableAndDateAfter(eventDay1.getId(),eventDay1.getDate());
        assertNotNull(found);
    }

    @Test(expected = EventDayDoesNotExistOrStateIsNotValid.class)
    public void getReservableAndBuyableAndDateAfter_EventDayDoesNotExist() throws EventDayDoesNotExist, EventDayDoesNotExistOrStateIsNotValid {
        Mockito.when(eventDaysRepository.findOneByIdAndStateAndDateAfter(eventDay1.getId(), eventDay1.getState(),eventDay1.getDate())).thenReturn(Optional.empty());
        EventDay not_found = eventDayService.getReservableAndBuyableAndDateAfter(eventDay2.getId(),eventDay1.getDate());
    }

    @Test(expected = EventDayDoesNotExistOrStateIsNotValid.class)
    public void getReservableAndBuyableAndDateAfter_StateIsNotValid() throws EventDayDoesNotExist, EventDayDoesNotExistOrStateIsNotValid {
        Mockito.when(eventDaysRepository.findOneByIdAndStateAndDateAfter(eventDay2.getId(), eventDay2.getState(),eventDay2.getDate())).thenReturn(Optional.ofNullable(eventDay2));
        EventDay found_notValidState = eventDayService.getReservableAndBuyableAndDateAfter(eventDay2.getId(),eventDay2.getDate());
    }

}
