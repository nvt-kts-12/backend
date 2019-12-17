package nvt.kts.ticketapp.service;

import nvt.kts.ticketapp.service.event.EventServiceIntegrationTestsSuite;
import nvt.kts.ticketapp.service.event.EventServiceUnitTestsSuite;
import nvt.kts.ticketapp.service.eventDay.EventDayUnitTestSuite;
import nvt.kts.ticketapp.service.location.LocationSchemeServiceIntegrationSuite;
import nvt.kts.ticketapp.service.location.LocationSchemeServiceUnitSuite;
import nvt.kts.ticketapp.service.reminders.RemindersSuite;
import nvt.kts.ticketapp.service.report.ReportServiceSuite;
import nvt.kts.ticketapp.service.sector.SectorServiceUnitSuite;
import nvt.kts.ticketapp.service.ticket.TicketServiceIntegrationTestsSuite;
import nvt.kts.ticketapp.service.ticket.TicketServiceUnitTestsSuite;
import nvt.kts.ticketapp.service.user.UserServiceIntegrationTestsSuite;
import nvt.kts.ticketapp.service.user.UserServiceUnitTestsSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EventServiceIntegrationTestsSuite.class,
        EventServiceUnitTestsSuite.class,
        UserServiceIntegrationTestsSuite.class,
        UserServiceUnitTestsSuite.class,
        EventDayUnitTestSuite.class,
        TicketServiceIntegrationTestsSuite.class,
        TicketServiceUnitTestsSuite.class,
        LocationSchemeServiceUnitSuite.class,
        LocationSchemeServiceIntegrationSuite.class,
        ReportServiceSuite.class,
        SectorServiceUnitSuite.class,
        RemindersSuite.class
})
public class ServiceSuite {
}
