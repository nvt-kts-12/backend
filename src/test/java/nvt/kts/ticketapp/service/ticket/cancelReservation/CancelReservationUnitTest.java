package nvt.kts.ticketapp.service.ticket.cancelReservation;

import nvt.kts.ticketapp.domain.dto.event.EventDayUpdateDTO;
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

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CancelReservationUnitTest {

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


        Mockito.when(ticketRepository.save(ticket)).thenReturn(ticket);

    }

    @Test
    public void cancelReservation_valid() throws TicketDoesNotExist, ReservationCanNotBeCancelled {

        Mockito.when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        TicketDTO cancelTicket = ticketService.cancelReservation(ticket.getId());
        assertNotNull(cancelTicket);
        assertThat(cancelTicket.getEventDayState().equals(EventDayState.RESERVABLE_AND_BUYABLE));
        assertThat(cancelTicket.getUser() == null);

    }

    @Test(expected = ReservationCanNotBeCancelled.class)
    public void cancelReservation_ExpectedReservationCannotBeCancelled() throws TicketDoesNotExist, ReservationCanNotBeCancelled {

        Mockito.when(ticketRepository.save(sold_ticket)).thenReturn(sold_ticket);
        Mockito.when(ticketRepository.findById(sold_ticket.getId())).thenReturn(Optional.of(sold_ticket));

        TicketDTO cancelTicket = ticketService.cancelReservation(sold_ticket.getId());
    }

    @Test(expected = TicketDoesNotExist.class)
    public void cancelReservation_TicketDoesNotExist() throws TicketDoesNotExist, ReservationCanNotBeCancelled {
        Mockito.when(ticketRepository.findById(ticket_not_in_database.getId())).thenReturn(Optional.of(ticket_not_in_database));
        TicketDTO cancelTicket = ticketService.cancelReservation(sold_ticket.getId());
    }

}
