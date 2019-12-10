package nvt.kts.ticketapp.service.ticket.show.reservations;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.Authority;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.repository.user.AuthorityRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import nvt.kts.ticketapp.service.ticket.TicketService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShowReservationsIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private LocationSchemeRepository locationSchemeRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventDaysRepository eventDaysRepository;

    @Before
    public void setUp() {

        Authority roleRegistered = new Authority("ROLE_REGISTERED");
        authorityRepository.save(roleRegistered);

        User user = new User("username", "password", "firstname", "lastname",
                "email@gmail.com");
        userRepository.save(user);
        user.setAuthorities(new ArrayList<>(){{add(roleRegistered);}});
        userRepository.save(user);

        LocationScheme locationScheme = new LocationScheme("locationname", "locationaddress");
        locationSchemeRepository.save(locationScheme);

        Location location = new Location(locationScheme);
        locationRepository.save(location);

        Event event = new Event("eventname", EventCategory.ENTERTAINMENT, "description");
        eventRepository.save(event);

        EventDay eventDay = new EventDay(new Date(System.currentTimeMillis() + 864000000), location,
                new Date(System.currentTimeMillis() + (86400000 * 5)), EventDayState.RESERVABLE_AND_BUYABLE, event);
        eventDaysRepository.save(eventDay);


        ticketRepository.save(new Ticket(false, 1L, 1, 1, 2000, eventDay, user, false));
        ticketRepository.save(new Ticket(false, 1L, 1, 2, 2000, eventDay, user, false));

    }

    @Test
    public void showReservations_success() {

        List<Ticket> reservations = ticketService.getReservationsFromUser(1L);

        assertNotNull(reservations);
        assertEquals(reservations.size(), 2);
        assertEquals(reservations.get(0).getSectorId(), Long.valueOf(1L));
        assertEquals(reservations.get(0).getSeatRow(), 1);
        assertEquals(reservations.get(0).getSeatCol(), 1);
        assertEquals(reservations.get(1).getSectorId(), Long.valueOf(1L));
        assertEquals(reservations.get(1).getSeatRow(), 1);
        assertEquals(reservations.get(1).getSeatCol(), 2);

        assertFalse(reservations.get(0).isSold());
        assertFalse(reservations.get(1).isSold());

    }

}
