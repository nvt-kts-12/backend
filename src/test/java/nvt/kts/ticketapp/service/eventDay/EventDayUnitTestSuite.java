package nvt.kts.ticketapp.service.eventDay;

import nvt.kts.ticketapp.service.eventDay.findById.FindEventDayByIdUnitTest;
import nvt.kts.ticketapp.service.eventDay.getByIdAndDateAfter.GetByIdAndDateAfterUnitTest;
import nvt.kts.ticketapp.service.eventDay.getReservableAndBuyableAndDateAfter.GetReservableAndBuyableAndDateAfterUnitTest;
import nvt.kts.ticketapp.service.eventDay.saveAll.SaveAllEventsUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SaveAllEventsUnitTest.class,
        FindEventDayByIdUnitTest.class,
        GetReservableAndBuyableAndDateAfterUnitTest.class,
        GetByIdAndDateAfterUnitTest.class

})
public class EventDayUnitTestSuite {
}
