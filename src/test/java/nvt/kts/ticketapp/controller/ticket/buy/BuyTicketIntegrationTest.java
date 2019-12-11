package nvt.kts.ticketapp.controller.ticket.buy;


import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.dto.user.UserDTO;
import nvt.kts.ticketapp.domain.dto.user.UserEditDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.Authority;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.ticket.TicketDoesNotExist;
import nvt.kts.ticketapp.exception.ticket.TicketNotFoundOrAlreadyBought;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
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
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class BuyTicketIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationSchemeRepository locationSchemeRepository;
    @Autowired
    private EventDaysRepository eventDaysRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private EventRepository eventRepository;

    private final String URL = "/api/ticket/buy";

    private static Ticket bought_ticket;
    private static Ticket ticket;
    private static EventDay eventDay;
    private static Location location;
    private static LocationScheme scheme;
    private static Event event;
    @Before
    public void setUp() {

        Authority roleRegistered = new Authority("ROLE_REGISTERED");
        authorityRepository.save(roleRegistered);

        User user = new User("username", "password", "firstname", "lastname",
                "email@gmail.com");
        userRepository.save(user);
        user.setAuthorities(new ArrayList<>(){{add(roleRegistered);}});
        userRepository.save(user);

        event = new Event("name", EventCategory.SPORT,"good");
        scheme = new LocationScheme("schemeName","address");
        location = new Location(scheme);
        eventDay = new EventDay(new Date(2-5-2020),location,new Date(2-3-2020), EventDayState.RESERVABLE_AND_BUYABLE,event);
        ticket = new Ticket(false,1L,1,1,100,eventDay,user,false);
        bought_ticket = new Ticket(true,1L,1,1,100,eventDay,user,false);

        eventRepository.save(event);
        locationSchemeRepository.save(scheme);
        locationRepository.save(location);
        eventDaysRepository.save(eventDay);
        ticketRepository.save(ticket);
        ticketRepository.save(bought_ticket);
    }

    @Test
    public void buyTicket_success(){
        ResponseEntity response = restTemplate.withBasicAuth("username", "password")
                .exchange(URL +"/"+ ticket.getId(),HttpMethod.PUT,null,TicketDTO.class);
    }

    @Test
    public void buyTicket_TicketNotFound(){
        ResponseEntity<String> response = restTemplate.withBasicAuth("username", "password")
                .exchange(URL +"/50",HttpMethod.PUT,null,String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ticket with id 50 not found or it is already bought.", response.getBody());
    }

    @Test
    public void buyTicket_TicketAlreadyBought(){
        ResponseEntity<String> response = restTemplate.withBasicAuth("username", "password")
                .exchange(URL +"/"+bought_ticket.getId(),HttpMethod.PUT,null,String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ticket with id 2 not found or it is already bought.", response.getBody());
    }


}
