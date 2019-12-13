package nvt.kts.ticketapp.controller.ticket;

import nvt.kts.ticketapp.controller.ticket.buy.BuyTicketIntegrationTest;
import nvt.kts.ticketapp.controller.ticket.show.bought.ShowBoughtIntegrationTest;
import nvt.kts.ticketapp.controller.ticket.show.reservations.ShowReservationsIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ShowReservationsIntegrationTest.class,
        ShowBoughtIntegrationTest.class,
        BuyTicketIntegrationTest.class
})
public class TicketControllerIntegrationTestsSuite {
}
