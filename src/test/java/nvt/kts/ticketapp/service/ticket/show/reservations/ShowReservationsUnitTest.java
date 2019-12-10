package nvt.kts.ticketapp.service.ticket.show.reservations;

import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.service.ticket.TicketService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShowReservationsUnitTest {

    @Autowired
    private TicketService ticketService;

    @MockBean
    private TicketRepository ticketRepository;

    @Before
    public void setUp() {

        User user = new User("testusername", "testpassword",
                "testfirstname", "testlastname", "testmail@gmail.com" );

        user.setId(1L);

        LocationScheme locationScheme = new LocationScheme("locationname", "locationaddress");

        Location location = new Location(locationScheme);

        Event event = new Event("eventname", EventCategory.ENTERTAINMENT, "description");

        EventDay eventDay = new EventDay(new Date(System.currentTimeMillis() + 864000000), location,
                new Date(System.currentTimeMillis() + (86400000 * 5)), EventDayState.RESERVABLE_AND_BUYABLE, event);

        ArrayList<Ticket> reservations = new ArrayList<Ticket>();

        reservations.add(new Ticket(false, 1L, 1, 1, 2000, eventDay, user, false));
        reservations.add(new Ticket(false, 1L, 1, 2, 2000, eventDay, user, false));

        Mockito.when(ticketRepository.findByUserIdAndSoldFalse(1L)).thenReturn(reservations);
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
