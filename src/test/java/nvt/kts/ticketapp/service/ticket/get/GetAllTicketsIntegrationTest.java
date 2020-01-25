package nvt.kts.ticketapp.service.ticket.get;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import nvt.kts.ticketapp.service.ticket.TicketService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetAllTicketsIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationSectorRepository locationSectorRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventDaysRepository eventDaysRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationSchemeRepository locationSchemeRepository;
    @Autowired
    private SectorRepository sectorRepository;


    private static Ticket ticket;
    private static Ticket ticket_not_in_database;
    private static Ticket sold_ticket;
    private static User user;
    private static EventDay eventDay;
    private static Event event;
    private static Location location;
    private static LocationScheme locationScheme;
    private static Sector sector;
    private static LocationSector locationSector;

    private final Long NONEXISTENT_SECTOR_ID = 5L;
    private final Long NONEXISTENT_EVENTDAY_ID = 8L;

    @Before
    public void setUp() {
        locationScheme = new LocationScheme("scheme", "novisad");
        sector = new Sector(0, 0, 0, 0, 10, 0, 0, SectorType.PARTER, locationScheme);
        location = new Location(locationScheme);
        locationSector = new LocationSector(sector, location, 150, 10, false);
        event = new Event("name1", EventCategory.ENTERTAINMENT, "good2");
        eventDay = new EventDay(new Date(2 - 1 - 2019), location, new Date(3 - 1 - 2019), EventDayState.RESERVABLE_AND_BUYABLE, event);
        user = new User("username", "pass", "name", "lastname", "email@gmail.com");

        ticket = new Ticket(false, 1L, 800, false, eventDay, user);
        sold_ticket = new Ticket(true, 1L, 800, false, eventDay, user);
        ticket_not_in_database = new Ticket(false, 1L, 800, false, eventDay, user);
        ticket_not_in_database.setId(12L);

        locationSchemeRepository.save(locationScheme);
        sectorRepository.save(sector);

        locationRepository.save(location);
        locationSectorRepository.save(locationSector);
        eventRepository.save(event);
        userRepository.save(user);
        eventDaysRepository.save(eventDay);
        ticketRepository.save(ticket);
        ticketRepository.save(sold_ticket);
    }

    @Test
    public void getAllTicketsForSectorAndEventDay_Positive() {
        List<TicketDTO> allTickets = ticketService.getAllTicketsForSectorAndEventDay(sector.getId(), eventDay.getId());

        assertNotNull(allTickets);
        assertEquals(2, allTickets.size());
    }

    @Test
    public void getAllTicketsForSectorAndEventDay_Negative_SectorId() {
        List<TicketDTO> allTickets = ticketService.getAllTicketsForSectorAndEventDay(NONEXISTENT_SECTOR_ID, eventDay.getId());

        assertNotNull(allTickets);
        assertEquals(0, allTickets.size());
    }

    @Test
    public void getAllTicketsForSectorAndEventDay_Negative_EventDayId() {
        List<TicketDTO> allTickets = ticketService.getAllTicketsForSectorAndEventDay(sector.getId(), NONEXISTENT_EVENTDAY_ID);

        assertNotNull(allTickets);
        assertEquals(0, allTickets.size());
    }
}
