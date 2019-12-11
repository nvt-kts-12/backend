package nvt.kts.ticketapp.controller.event;

import nvt.kts.ticketapp.controller.event.searchAndFilter.SearchAndFilterIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CreateEventIntegrationTest.class,
        SearchAndFilterIntegrationTest.class
})
public class EventControllerIntegrationTestsSuite {
}
