package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.service.event.create.CreateEventIntegrationTest;
import nvt.kts.ticketapp.service.event.getAll.GetAllEventsIntegrationTest;
import nvt.kts.ticketapp.service.event.reserve.ReserveEventIntegrationTest;
import nvt.kts.ticketapp.service.event.update.UpdateEventIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CreateEventIntegrationTest.class,
        UpdateEventIntegrationTest.class,
        UpdateEventIntegrationTest.class,
        GetAllEventsIntegrationTest.class,
        ReserveEventIntegrationTest.class
})
public class EventServiceIntegrationTestsSuite {
}
