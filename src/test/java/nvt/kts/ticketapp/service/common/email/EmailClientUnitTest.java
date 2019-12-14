package nvt.kts.ticketapp.service.common.email;

import com.google.zxing.WriterException;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmailClientUnitTest {

    private static final String EMAIL_SUBJECT = "Test subject.";


    @Autowired
    private EmailClient emailClient;

    @MockBean
    private JavaMailSender javaMailSender;

    private final Long EXISTING_TICKET_ID = 1L;
    private final Long EXISTING_TICKET_02_ID = 2L;
    private final Long EXISTING_SECTOR_ID = 1L;
    private final String USER_EMAIL = "ljubicjanko1@gmail.com";

    private static Ticket ticket;
    private static Ticket ticket_02;
    private static List<Ticket> ticketList;

    @Before
    public void setUp() throws Exception {
        User user = new User("janko_lj", "12345678",
                "Janko", "Ljubic", USER_EMAIL);

        LocationScheme locationScheme = new LocationScheme("Scheme 1", "Address 1");
        Location location = new Location(locationScheme);

        Event event = new Event("Event 1", EventCategory.SPORT, "event description");

        EventDay eventDay = new EventDay(incDate(2), location, incDate(-2),
                EventDayState.RESERVABLE_AND_BUYABLE, event);

        ticket = new Ticket(true, EXISTING_SECTOR_ID, 1, 1, 150.0
                , eventDay, user, false);
        ticket.setId(EXISTING_TICKET_ID);

        ticket_02 = new Ticket(true, EXISTING_SECTOR_ID, 1, 0, 150.0
                , eventDay, user, true);
        ticket_02.setId(EXISTING_TICKET_02_ID);

        ticketList = Arrays.asList(ticket, ticket_02);
    }

    @Test
    public void sendMimeEmail_Positive() throws IOException, WriterException {
        emailClient.generateQrCode(String.valueOf(EXISTING_TICKET_ID));
        emailClient.generateQrCode(String.valueOf(EXISTING_TICKET_02_ID));

        emailClient.sendMimeEmail(USER_EMAIL, EMAIL_SUBJECT,
                "msg", Arrays.asList(ticket, ticket_02));

        verify(javaMailSender, times(1)).send(any(MimeMessagePreparator.class));
    }



    /**
     * private method that increases or decreases todays date
     *
     * @param byDays
     * @return
     */
    private Date incDate(int byDays) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, byDays);
        return cal.getTime();
    }
}