package nvt.kts.ticketapp.e2e;

import nvt.kts.ticketapp.e2e.test.SearchAndFilterTest;
import nvt.kts.ticketapp.e2e.test.reservationAndBuying.BuyingTest;
import nvt.kts.ticketapp.e2e.test.reservationAndBuying.ReservationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SearchAndFilterTest.class,
        BuyingTest.class,
        ReservationTest.class
})
public class E2ETestSuite {
}
