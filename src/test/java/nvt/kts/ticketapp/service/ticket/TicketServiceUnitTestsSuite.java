package nvt.kts.ticketapp.service.ticket;

import nvt.kts.ticketapp.service.event.create.CreateEventUnitTest;
import nvt.kts.ticketapp.service.event.getAll.GetAllEventsUnitTest;
import nvt.kts.ticketapp.service.event.update.UpdateEventDayUnitTest;
import nvt.kts.ticketapp.service.event.update.UpdateEventUnitTest;
import nvt.kts.ticketapp.service.ticket.buy.BuyTicketUnitTest;
import nvt.kts.ticketapp.service.ticket.cancelReservation.CancelReservationUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BuyTicketUnitTest.class,
        CancelReservationUnitTest.class
})
public class TicketServiceUnitTestsSuite {
}
