package nvt.kts.ticketapp.controller.event.update;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayUpdateDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.user.AuthorityRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class UpdateEventDayIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    private final String URL = "/api/event/eventDay";

    @Autowired
    private EventDaysRepository eventDaysRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationSchemeRepository locationSchemeRepository;

    private static Event event;
    private static EventDay eventDay1;
    private static EventDayUpdateDTO eventDay2;
    private static EventDayUpdateDTO eventDayDetails;
    private static Location location;
    private static LocationScheme locationScheme;
    private static EventDayUpdateDTO eventDayDetails_badDateFormat;

    @Before
    public void setUp(){
        locationScheme = new LocationScheme("schemeName","address");
        location= new Location(locationScheme);
        event= new Event("name4", EventCategory.SPORT, "good4");
        eventDay1 = new EventDay(new Date(2020-01-28),location,new Date(2020-01-27), EventDayState.RESERVABLE_AND_BUYABLE,event);
        eventDayDetails = new EventDayUpdateDTO(1L,"2020-01-12","2020-01-10",EventDayState.SOLD_OUT);
        eventDayDetails_badDateFormat = new EventDayUpdateDTO(1L,"12-01-2020","10-01-2020",EventDayState.SOLD_OUT);

        locationSchemeRepository.save(locationScheme);
        locationRepository.save(location);
        eventRepository.save(event);
        eventDaysRepository.save(eventDay1);
    }

    /*              BACA UNSEPARABLE DATE I DATE FORMAT IS NOT VALID I TEST PADA
    @Test
    public void updateEventDay_DateFormatNotValid() {
                                                                             //bad format
        EventDayUpdateDTO eventDayDetails = new EventDayUpdateDTO(1L,"10.01.2020","2020-05-03",EventDayState.NOT_IN_SALE);

        HttpEntity<EventDayUpdateDTO> eventDayEditDTOHttpEntity = new HttpEntity<EventDayUpdateDTO>(eventDayDetails);

        ResponseEntity<String> eventResponse = restTemplate.withBasicAuth("admin", "password")
                .exchange(URL + "/" + eventDay1.getId(), HttpMethod.PUT,
                        eventDayEditDTOHttpEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, eventResponse.getStatusCode());
        assertEquals("Date format is not valid.", eventResponse.getBody());
    }
*/
    @Test
    public void updateEventDay_success() {

        EventDayUpdateDTO eventDayDetails = new EventDayUpdateDTO(1L,"2020-05-05","2020-05-03",EventDayState.NOT_IN_SALE);

        HttpEntity<EventDayUpdateDTO> eventDayEditDTOHttpEntity = new HttpEntity<EventDayUpdateDTO>(eventDayDetails);

        ResponseEntity<EventDayUpdateDTO> eventResponse = restTemplate.withBasicAuth("admin", "password")
                .exchange(URL + "/" + eventDay1.getId(), HttpMethod.PUT,
                        eventDayEditDTOHttpEntity, EventDayUpdateDTO.class);

        assertEquals(HttpStatus.OK, eventResponse.getStatusCode());
        assertNotNull(eventResponse.getBody());
        assertEquals(eventDayDetails.getEventDayState(), eventResponse.getBody().getEventDayState());
    }
}
