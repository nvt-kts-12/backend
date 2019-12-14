package nvt.kts.ticketapp.service.ticket.buy;

import com.google.zxing.WriterException;
import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.ticket.TicketListCantBeEmpty;
import nvt.kts.ticketapp.exception.ticket.TicketNotFoundOrAlreadyBought;
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

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BuyTicketIntegrationTest {
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

        locationSchemeRepository.save(locationScheme);
        locationRepository.save(location);
        eventRepository.save(event);
        userRepository.save(user);
        eventDaysRepository.save(eventDay);
        ticketRepository.save(ticket);
    }

    @Test
    public void buyTicketTest() throws WriterException, IOException, TicketNotFoundOrAlreadyBought, TicketListCantBeEmpty {

        Ticket bought = ticketService.buyTicket(ticket.getId());
        assertNotNull(bought);
        assertTrue(bought.isSold());

    }

    @Test(expected = TicketNotFoundOrAlreadyBought.class)
    public void buyTicketTest_TicketNotFound() throws WriterException, IOException, TicketNotFoundOrAlreadyBought, TicketListCantBeEmpty {
        Ticket bought = ticketService.buyTicket(sold_ticket.getId());
    }
}
