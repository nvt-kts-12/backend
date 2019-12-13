package nvt.kts.ticketapp.controller.event.update;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.user.UserDTO;
import nvt.kts.ticketapp.domain.dto.user.UserEditDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.user.Authority;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.repository.event.EventRepository;
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

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class UpdateEventIntegrationTest {

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

    private final String URL = "/api/event";
    private static Event eventForUpdate;

    @Before
    public void setUp() {

        Authority role_admin = new Authority("ADMIN");
        authorityRepository.save(role_admin);

        User user = new User("admin", "password", "firstname", "lastname",
                "email@gmail.com");
        userRepository.save(user);
        user.setAuthorities(new ArrayList<>(){{add(role_admin);}});
        userRepository.save(user);

        eventForUpdate = new Event("nameee",EventCategory.SPORT,"goood");
        eventRepository.save(eventForUpdate);


    }

    @Test
    public void updateEvent_success() {

        EventDTO eventDetails = new EventDTO(1L,"good","namee", EventCategory.ENTERTAINMENT);

        HttpEntity<EventDTO> eventEditDTOHttpEntity = new HttpEntity<EventDTO>(eventDetails);

        ResponseEntity<EventDTO> eventResponse = restTemplate.withBasicAuth("admin", "password")
                .exchange(URL + "/" + eventForUpdate.getId(), HttpMethod.PUT,
                        eventEditDTOHttpEntity, EventDTO.class);

        assertEquals(HttpStatus.OK, eventResponse.getStatusCode());
        assertNotNull(eventResponse.getBody());
        assertEquals(eventDetails.getCategory(), eventResponse.getBody().getCategory());
        assertEquals(eventDetails.getName(), eventResponse.getBody().getName());
        assertEquals(eventDetails.getDescription(), eventResponse.getBody().getDescription());
    }

    @Test
    public void updateEvent_EventNotFound() {

        EventDTO eventDetails = new EventDTO(1L,"good","namee", EventCategory.ENTERTAINMENT);

        HttpEntity<EventDTO> eventEditDTOHttpEntity = new HttpEntity<EventDTO>(eventDetails);

        ResponseEntity<String> eventResponse = restTemplate.withBasicAuth("admin", "password")
                .exchange(URL + "/50", HttpMethod.PUT,
                        eventEditDTOHttpEntity,  String.class);

        assertEquals(HttpStatus.BAD_REQUEST, eventResponse.getStatusCode());
        assertEquals("Event with id: 50 does not exist.", eventResponse.getBody());
    }
}
