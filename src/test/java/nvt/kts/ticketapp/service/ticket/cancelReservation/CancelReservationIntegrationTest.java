package nvt.kts.ticketapp.service.ticket.cancelReservation;


import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.ticket.ReservationCanNotBeCancelled;
import nvt.kts.ticketapp.exception.ticket.TicketDoesNotExist;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CancelReservationIntegrationTest {
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
    private EventRepository eventRepository;
    @Autowired
    private EventDaysRepository eventDaysRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationSchemeRepository locationSchemeRepository;


    private static Ticket ticket;
    private static Ticket ticket_not_in_database;
    private static Ticket sold_ticket;
    private static User user;
    private static EventDay eventDay;
    private static Event event;
    private static Location location;
    private static LocationScheme locationScheme;

    @Before
    public void setUp(){
        locationScheme = new LocationScheme("scheme","novisad");
        location = new Location(locationScheme);
        event = new Event("name1", EventCategory.ENTERTAINMENT, "good2");
        eventDay = new EventDay(new Date(2-1-2019),location,new Date(3-1-2019), EventDayState.RESERVABLE_AND_BUYABLE,event);
        user = new User("username","pass","name","lastname","email@gmail.com");

        ticket = new Ticket(false,1L,800,false ,eventDay,user);
        sold_ticket = new Ticket(true,1L,800,false ,eventDay,user);
        ticket_not_in_database = new Ticket(false,1L,800,false ,eventDay,user);
        ticket_not_in_database.setId(12L);

        locationSchemeRepository.save(locationScheme);
        locationRepository.save(location);
        eventRepository.save(event);
        userRepository.save(user);
        eventDaysRepository.save(eventDay);
        ticketRepository.save(ticket);
        ticketRepository.save(sold_ticket);

    }

    @Test
    public void cancelReservationTest() throws TicketDoesNotExist, ReservationCanNotBeCancelled {

        TicketDTO canceled = ticketService.cancelReservation(ticket.getId());
        assertNotNull(canceled);
        assertThat(canceled.getEventDayState().equals(EventDayState.RESERVABLE_AND_BUYABLE));
        assertThat(canceled.getUser() == null);

    }

    @Test(expected = TicketDoesNotExist.class)
    public void cancelReservationTest_ticketDoesNotExist() throws TicketDoesNotExist, ReservationCanNotBeCancelled {
        TicketDTO canceled = ticketService.cancelReservation(ticket_not_in_database.getId());
    }

    @Test(expected = ReservationCanNotBeCancelled.class)
    public void cancelReservationTest_ReservationCanNotBeCancelled() throws TicketDoesNotExist, ReservationCanNotBeCancelled {
        TicketDTO canceled = ticketService.cancelReservation(sold_ticket.getId());
    }
}
