package nvt.kts.ticketapp.service.ticket.buy;

import com.google.zxing.WriterException;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.ticket.TicketNotFoundOrAlreadyBought;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.service.ticket.TicketService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BuyTicketUnitTest {

    @MockBean
    TicketRepository ticketRepository;

    @Autowired
    TicketService ticketService;

    private static Ticket ticket;
    private static Ticket sold_ticket;
    private static User user;
    private static EventDay eventDay;
    private static Event event;
    private static Location location;
    private static LocationScheme locationScheme;

    @Before
    public void setUp() {

        locationScheme = new LocationScheme("scheme","novisad");
        location = new Location(locationScheme);
        event = new Event("name1", EventCategory.ENTERTAINMENT, "good2");
        eventDay = new EventDay(new Date(2-1-2019),location,new Date(3-1-2019), EventDayState.RESERVABLE_AND_BUYABLE,event);
        user = new User("username","pass","name","lastname","email@gmail.com");

        ticket = new Ticket(false,1L,800,false ,eventDay,user);
        ticket.setId(100L);
        sold_ticket = new Ticket(true,1L,800,false ,eventDay,user);

        Mockito.when(ticketRepository.save(ticket)).thenReturn(ticket);

    }

    @Test
    public void buyTicketTest() throws WriterException, IOException, TicketNotFoundOrAlreadyBought {

        Mockito.when(ticketRepository.findOneByIdAndSoldFalse(ticket.getId())).thenReturn(Optional.of(ticket));
        Ticket bought = ticketService.buyTicket(ticket.getId());
        assertThat(HttpStatus.OK);
        assertTrue(bought.isSold());
    }

    @Test(expected = TicketNotFoundOrAlreadyBought.class)
    public void buyTicket_TicketNotFoundOrAlreadyBought() throws WriterException, IOException, TicketNotFoundOrAlreadyBought {
        Long ticketId= 100L;
        Mockito.when(ticketRepository.findOneByIdAndSoldFalse(ticketId)).thenReturn(Optional.empty());

        ticketService.buyTicket(ticketId);
    }

    @Test(expected = TicketNotFoundOrAlreadyBought.class)
    public void buyTicket_TicketNotFoundOrAlreadyBought2() throws WriterException, IOException, TicketNotFoundOrAlreadyBought {
        Mockito.when(ticketRepository.findOneByIdAndSoldFalse(sold_ticket.getId())).thenReturn(Optional.empty());

        ticketService.buyTicket(sold_ticket.getId());
    }

}
