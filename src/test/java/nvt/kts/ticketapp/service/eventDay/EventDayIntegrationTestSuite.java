package nvt.kts.ticketapp.service.eventDay;

import nvt.kts.ticketapp.service.eventDay.findById.FindEventDayByIdIntegrationTest;
import nvt.kts.ticketapp.service.eventDay.getReservableAndBuyableAndDateAfter.GetReservableAndBuyableAndDateAfterIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        FindEventDayByIdIntegrationTest.class,
        GetReservableAndBuyableAndDateAfterIntegrationTest.class

})
public class EventDayIntegrationTestSuite {
}
