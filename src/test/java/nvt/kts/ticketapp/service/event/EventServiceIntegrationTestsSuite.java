package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.service.event.create.CreateEventIntegrationTest;
import nvt.kts.ticketapp.service.event.getAll.GetAllEventsIntegrationTest;
import nvt.kts.ticketapp.service.event.update.UpdateEventIntegrationTest;
import nvt.kts.ticketapp.service.event.update.UpdateEventUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CreateEventIntegrationTest.class,
        UpdateEventIntegrationTest.class,
        UpdateEventIntegrationTest.class,
        GetAllEventsIntegrationTest.class
})
public class EventServiceIntegrationTestsSuite {
}
