package nvt.kts.ticketapp.service.ticket.get;

import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.service.event.EventService;
import nvt.kts.ticketapp.service.ticket.TicketService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetAllTicketsUnitTest {

    @MockBean
    private EventDaysRepository eventDaysRepository;

    @MockBean
    private TicketRepository ticketRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    TicketService ticketService;



    private static Ticket ticket;
    private static Ticket sold_ticket;
    private static User user;
    private static EventDay eventDay;
    private static Event event;
    private static Location location;
    private static LocationScheme locationScheme;
    private static Ticket ticket_not_in_database;


    public static final Long EXISTING_SECTOR_ID = 1L;
    public static final Long NONEXISTENT_SECTOR_ID = 2L;
    public static final Long NONEXISTENT_EVENTDAY_ID = 3L;


    @Before
    public void setUp() {

        locationScheme = new LocationScheme("scheme","novisad");
        location = new Location(locationScheme);
        event = new Event("name1", EventCategory.ENTERTAINMENT, "good2");
        eventDay = new EventDay(new Date(2-1-2019),location,new Date(3-1-2019), EventDayState.RESERVABLE_AND_BUYABLE,event);
        user = new User("username","pass","name","lastname","email@gmail.com");
        user.setId(1L);


        ticket = new Ticket(false,1L,800,false ,eventDay,user);
        ticket.setId(100L);
        sold_ticket = new Ticket(true,1L,800,false ,eventDay,user);
        ticket_not_in_database = new Ticket(false,1L,800,false ,eventDay,user);
        ticket_not_in_database.setId(100L);


        Mockito.when(ticketRepository.findAllBySectorIdAndEventDayIdAndDeletedFalse(EXISTING_SECTOR_ID, eventDay.getId()))
                .thenReturn(Arrays.asList(ticket, sold_ticket));
    }

    @Test
    public void getAllTicketsForSectorAndEventDay_Positive(){
        List<TicketDTO> tickets = ticketService.getAllTicketsForSectorAndEventDay(EXISTING_SECTOR_ID, eventDay.getId());

        assertNotNull(tickets);
        assertEquals(2, tickets.size());
    }

    @Test
    public void getAllTicketsForSectorAndEventDay_Negative_SectorId(){
        List<TicketDTO> tickets = ticketService.getAllTicketsForSectorAndEventDay(NONEXISTENT_SECTOR_ID, eventDay.getId());

        assertNotNull(tickets);
        assertEquals(0, tickets.size());
    }

    @Test
    public void getAllTicketsForSectorAndEventDay_Negative_EventDayId(){
        List<TicketDTO> tickets = ticketService.getAllTicketsForSectorAndEventDay(EXISTING_SECTOR_ID, NONEXISTENT_EVENTDAY_ID);

        assertNotNull(tickets);
        assertEquals(0, tickets.size());
    }
}
