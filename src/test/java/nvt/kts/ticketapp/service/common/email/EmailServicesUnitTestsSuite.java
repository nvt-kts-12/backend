package nvt.kts.ticketapp.service.common.email;


import nvt.kts.ticketapp.service.common.email.ticket.ReservationsReminderServiceUnitTest;
import nvt.kts.ticketapp.service.common.email.ticket.SoldTicketsReminderServiceUnitTest;
import nvt.kts.ticketapp.service.common.email.ticket.TicketEmailServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EmailClientUnitTest.class,
        ReservationsReminderServiceUnitTest.class,
        SoldTicketsReminderServiceUnitTest.class,
        TicketEmailServiceUnitTest.class
})
public class EmailServicesUnitTestsSuite {
}
