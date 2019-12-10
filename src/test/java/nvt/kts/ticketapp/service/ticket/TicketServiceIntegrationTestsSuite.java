package nvt.kts.ticketapp.service.ticket;

import nvt.kts.ticketapp.service.event.create.CreateEventUnitTest;
import nvt.kts.ticketapp.service.event.getAll.GetAllEventsUnitTest;
import nvt.kts.ticketapp.service.event.update.UpdateEventDayUnitTest;
import nvt.kts.ticketapp.service.event.update.UpdateEventUnitTest;
import nvt.kts.ticketapp.service.ticket.buy.BuyTicketIntegrationTest;
import nvt.kts.ticketapp.service.ticket.cancelReservation.CancelReservationIntegrationTest;
import nvt.kts.ticketapp.service.ticket.cancelReservation.CancelReservationUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
       BuyTicketIntegrationTest.class,
       CancelReservationIntegrationTest.class
})
public class TicketServiceIntegrationTestsSuite {
}

