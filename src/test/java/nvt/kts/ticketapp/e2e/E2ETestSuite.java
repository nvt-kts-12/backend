package nvt.kts.ticketapp.e2e;

import nvt.kts.ticketapp.e2e.test.SearchAndFilterTest;
import nvt.kts.ticketapp.e2e.test.BuyingTest;
import nvt.kts.ticketapp.e2e.test.ReservationTest;
import nvt.kts.ticketapp.e2e.test.UserTicketsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SearchAndFilterTest.class,
        BuyingTest.class,
        ReservationTest.class,
        UserTicketsTest.class
})
public class E2ETestSuite {
}
