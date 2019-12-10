package nvt.kts.ticketapp.service.eventDay.saveAll;

import nvt.kts.ticketapp.domain.dto.event.EventDayUpdateDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.service.event.EventDayService;
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
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SaveAllEventsUnitTest {

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


        eventList = new ArrayList<EventDay>();
        eventList.add(eventDay1);
        eventList.add(eventDay2);

        Mockito.when(eventDaysRepository.saveAll(eventList)).thenReturn(eventList);
    }

    @Test
    public void saveAll(){
        eventDayService.saveAll(eventList);

    }


}
