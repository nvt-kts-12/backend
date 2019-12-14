package nvt.kts.ticketapp.service.reminders;

import com.google.zxing.WriterException;
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
import nvt.kts.ticketapp.service.common.email.ticket.ReservationsReminderService;
import nvt.kts.ticketapp.service.common.email.ticket.SoldTicketsReminderService;
import nvt.kts.ticketapp.service.reminder.ReminderService;
import org.junit.Before;
import org.junit.Rule;
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
public class ReminderServiceIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private ReminderService reminderService;

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

        EventDay eventDay = new EventDay(new Date(System.currentTimeMillis() + (86400000 * 3)), location,
                new Date(System.currentTimeMillis() + 86400000), EventDayState.RESERVABLE_AND_BUYABLE, event);
        eventDaysRepository.save(eventDay);


        ticketRepository.save(new Ticket(true, 1L, 1, 1, 2000, eventDay, user, false));
        ticketRepository.save(new Ticket(true, 1L, 1, 2, 2000, eventDay, user, false));

        Event event2 = new Event("eventname2", EventCategory.ENTERTAINMENT, "description2");
        eventRepository.save(event2);

        EventDay eventDay2 = new EventDay(new Date(System.currentTimeMillis() + (86400000 * 7)), location,
                new Date(System.currentTimeMillis() + (86400000 * 3)), EventDayState.RESERVABLE_AND_BUYABLE, event2);
        eventDaysRepository.save(eventDay2);

        ticketRepository.save(new Ticket(false, 1L, 1, 1, 2000, eventDay2, user, false));

    }

    @Test
    public void sendReminders() throws IOException, WriterException {

        reminderService.sendReminders();

    }
}
