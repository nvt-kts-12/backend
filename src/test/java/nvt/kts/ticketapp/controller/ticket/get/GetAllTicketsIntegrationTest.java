package nvt.kts.ticketapp.controller.ticket.get;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.Authority;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class GetAllTicketsIntegrationTest {

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
    private SectorRepository sectorRepository;
    @Autowired
    private EventDaysRepository eventDaysRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationSectorRepository locationSectorRepository;
    @Autowired
    private EventRepository eventRepository;

    private final String URL = "/api/ticket/";

    private static Ticket bought_ticket;
    private static Ticket ticket;
    private static EventDay eventDay;
    private static Location location;
    private static LocationSector locationSector;
    private static LocationScheme scheme;
    private static Sector sector;
    private static Event event;

    private final Long NONEXISTENT_SECTOR_ID = 5L;
    private final Long NONEXISTENT_EVENTDAY_ID = 8L;

    @Before
    public void setUp() {

        Authority roleRegistered = new Authority("ROLE_REGISTERED");
        authorityRepository.save(roleRegistered);

        User user = new User("username", "password", "firstname", "lastname",
                "email@gmail.com");
        userRepository.save(user);
        user.setAuthorities(new ArrayList<>() {{
            add(roleRegistered);
        }});
        userRepository.save(user);

        event = new Event("name", EventCategory.SPORT, "good");
        scheme = new LocationScheme("schemeName", "address");
        sector = new Sector(0, 0, 0, 0, 10, 0, 0, SectorType.PARTER, scheme);
        location = new Location(scheme);
        locationSector = new LocationSector(sector, location, 150, 10, false);
        eventDay = new EventDay(new Date(2 - 5 - 2020), location, new Date(2 - 3 - 2020), EventDayState.RESERVABLE_AND_BUYABLE, event);

        eventRepository.save(event);
        locationSchemeRepository.save(scheme);
        sectorRepository.save(sector);

        locationRepository.save(location);
        locationSectorRepository.save(locationSector);

        eventDaysRepository.save(eventDay);

        ticket = new Ticket(false, sector.getId(), 0, 0, locationSector.getPrice(), eventDay, user, false);
        bought_ticket = new Ticket(true, sector.getId(), 0, 0, locationSector.getPrice(), eventDay, user, false);

        ticketRepository.save(ticket);
        ticketRepository.save(bought_ticket);
    }

    @Test
    public void getBySectorAndDay_Positive() {
        String url = URL + sector.getId() + "/" + eventDay.getId();
        ResponseEntity<List<TicketDTO>> response = restTemplate.withBasicAuth("username", "password")
                .exchange(url , HttpMethod.GET, null, new ParameterizedTypeReference<List<TicketDTO>>() {
                });

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void getBySectorAndDay_Negative(){
        String url = URL + NONEXISTENT_SECTOR_ID + "/" + eventDay.getId();
        ResponseEntity<List<TicketDTO>> response = restTemplate.withBasicAuth("username", "password")
                .exchange(url , HttpMethod.GET, null, new ParameterizedTypeReference<List<TicketDTO>>() {
                });

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }


    @Test
    public void getBySectorAndDay_Negative_EventDayId(){
        String url = URL + sector.getId() + "/" + NONEXISTENT_EVENTDAY_ID;
        ResponseEntity<List<TicketDTO>> response = restTemplate.withBasicAuth("username", "password")
                .exchange(url , HttpMethod.GET, null, new ParameterizedTypeReference<List<TicketDTO>>() {
                });

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }
}
