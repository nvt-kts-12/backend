package nvt.kts.ticketapp.controller.event;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CreateEventIntegrationTest.class,
        ReserveEventIntegrationTest.class
})
public class EventControllerIntegrationTestsSuite {
}
