package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.service.event.create.CreateEventUnitTest;
import nvt.kts.ticketapp.service.event.update.UpdateEventUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    CreateEventUnitTest.class,
    UpdateEventUnitTest.class
})
public class EventServiceUnitTestsSuite {
}
