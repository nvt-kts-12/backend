package nvt.kts.ticketapp.controller;

import nvt.kts.ticketapp.controller.auth.authenticationController.AuthenticationControllerIntegrationTestsSuite;
import nvt.kts.ticketapp.controller.auth.authenticationController.AuthenticationControllerUnitTestsSuite;
import nvt.kts.ticketapp.controller.event.EventControllerIntegrationTestsSuite;
import nvt.kts.ticketapp.controller.location.LocationSchemeSectorControllerUnitSuite;
import nvt.kts.ticketapp.controller.report.ReportsUnitSuite;
import nvt.kts.ticketapp.controller.ticket.TicketControllerIntegrationTestsSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuthenticationControllerIntegrationTestsSuite.class,
        AuthenticationControllerUnitTestsSuite.class,
        EventControllerIntegrationTestsSuite.class,
        TicketControllerIntegrationTestsSuite.class,
        LocationSchemeSectorControllerUnitSuite.class,
        ReportsUnitSuite.class
})
public class ControllerSuite {
}
