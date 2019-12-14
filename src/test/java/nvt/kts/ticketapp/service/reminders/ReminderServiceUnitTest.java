package nvt.kts.ticketapp.service.reminders;

import com.google.zxing.WriterException;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.service.common.email.ticket.ReservationsReminderService;
import nvt.kts.ticketapp.service.common.email.ticket.SoldTicketsReminderService;
import nvt.kts.ticketapp.service.reminder.ReminderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReminderServiceUnitTest {

    @Autowired
    private ReminderService reminderService;

    @MockBean
    private SoldTicketsReminderService soldTicketsReminderServiceMocked;

    @MockBean
    private ReservationsReminderService reservationsReminderServiceMocked;

    @MockBean
    private TicketRepository ticketRepositoryMocked;

    @Before
    public void setUp() {

        User user = new User("testusername", "testpassword",
                "testfirstname", "testlastname", "testmail@gmail.com" );

        user.setId(1L);

        LocationScheme locationScheme = new LocationScheme("locationname", "locationaddress");

        Location location = new Location(locationScheme);

        Event event = new Event("eventname", EventCategory.ENTERTAINMENT, "description");

        EventDay eventDay = new EventDay(new Date(System.currentTimeMillis() + (86400000 * 3)), location,
                new Date(System.currentTimeMillis() + 86400000), EventDayState.RESERVABLE_AND_BUYABLE, event);

        ArrayList<Ticket> tickets = new ArrayList<Ticket>();

        tickets.add(new Ticket(true, 1L, 1, 1, 2000, eventDay, user, false));
        tickets.add(new Ticket(true, 1L, 1, 2, 2000, eventDay, user, false));

        Mockito.when(ticketRepositoryMocked.findAllBySoldTrueAndUserNotNull()).thenReturn(tickets);

        Event event2 = new Event("eventname2", EventCategory.ENTERTAINMENT, "description2");
        EventDay eventDay2 = new EventDay(new Date(System.currentTimeMillis() + (86400000 * 7)), location,
                new Date(System.currentTimeMillis() + (86400000 * 3)), EventDayState.RESERVABLE_AND_BUYABLE, event);

        ArrayList<Ticket> reservations = new ArrayList<>();

        reservations.add(new Ticket(false, 1L, 1, 1, 2000, eventDay2, user, false));

        Mockito.when(ticketRepositoryMocked.findAllBySoldFalseAndUserNotNull()).thenReturn(reservations);
    }

    @Test
    public void sendReminders() throws IOException, WriterException {

        reminderService.sendReminders();

        Mockito.verify(soldTicketsReminderServiceMocked,
                Mockito.times(2)).
                sendReminderForSoldTicket(Mockito.eq("testmail@gmail.com"), Mockito.any(Ticket.class));

        Mockito.verify(reservationsReminderServiceMocked,
                Mockito.times(1)).
                sendReminderForExpiringReservation(Mockito.eq("testmail@gmail.com"), Mockito.any(Ticket.class));

    }

}
