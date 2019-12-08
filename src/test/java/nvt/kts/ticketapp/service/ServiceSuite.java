package nvt.kts.ticketapp.service;

import nvt.kts.ticketapp.service.event.EventServiceIntegrationTestsSuite;
import nvt.kts.ticketapp.service.event.EventServiceUnitTestsSuite;
import nvt.kts.ticketapp.service.user.UserServiceIntegrationTestsSuite;
import nvt.kts.ticketapp.service.user.UserServiceUnitTestsSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EventServiceIntegrationTestsSuite.class,
        EventServiceUnitTestsSuite.class,
        UserServiceIntegrationTestsSuite.class,
        UserServiceUnitTestsSuite.class
})
public class ServiceSuite {
}
