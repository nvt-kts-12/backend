package nvt.kts.ticketapp.service.eventDay.findById;

import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExist;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FindEventDayByIdUnitTest {

    @MockBean
    private EventDaysRepository eventDaysRepository;

    @Autowired
    private EventDayService eventDayService;

    private static Event event;
    private static EventDay eventDay1;
    private static EventDay eventDay2;
    private static Location location;
    private static LocationScheme locationScheme;
    private static List<EventDay> eventList;

    @Before
    public void setUp() {
        locationScheme = new LocationScheme("schemeName", "address");
        location = new Location(locationScheme);
        event = new Event("name4", EventCategory.SPORT, "good4");
        eventDay1 = new EventDay(new Date(2020 - 01 - 15), location, new Date(2020 - 01 - 12), EventDayState.RESERVABLE_AND_BUYABLE, event);
        eventDay2 = new EventDay(new Date(2020 - 01 - 12), location, new Date(2020 - 01 - 10), EventDayState.NOT_IN_SALE, event);


        Mockito.when(eventDaysRepository.findById(eventDay1.getId())).thenReturn(java.util.Optional.ofNullable(eventDay1));
       //Mockito.when(eventDaysRepository.saveAll(eventList)).thenReturn(eventList);
    }

    @Test
    public void findById_success() throws EventDayDoesNotExist {
        EventDay found_eventDay= eventDayService.findOneById(eventDay1.getId());
        assertNotNull(found_eventDay);

        assertEquals(found_eventDay.getDate(),eventDay1.getDate());
        assertEquals(found_eventDay.getLocation(),eventDay1.getLocation());
        assertEquals(found_eventDay.getState(),eventDay1.getState());
    }

    @Test(expected = EventDayDoesNotExist.class)
    public void findById_EventDayDoesNotExist() throws EventDayDoesNotExist {
        Mockito.when(eventDaysRepository.findById(eventDay2.getId())).thenReturn(Optional.empty());
        EventDay found_eventDay= eventDayService.findOneById(eventDay2.getId());
    }

}
