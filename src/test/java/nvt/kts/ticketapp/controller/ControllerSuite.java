package nvt.kts.ticketapp.controller;

import nvt.kts.ticketapp.controller.auth.authenticationController.AuthenticationControllerIntegrationTestsSuite;
import nvt.kts.ticketapp.controller.auth.authenticationController.AuthenticationControllerUnitTestsSuite;
import nvt.kts.ticketapp.controller.event.EventControllerIntegrationTestsSuite;
import nvt.kts.ticketapp.controller.ticket.TicketControllerIntegrationTestsSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuthenticationControllerIntegrationTestsSuite.class,
        AuthenticationControllerUnitTestsSuite.class,
        EventControllerIntegrationTestsSuite.class,
        TicketControllerIntegrationTestsSuite.class
})
public class ControllerSuite {
}
