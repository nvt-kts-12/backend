package nvt.kts.ticketapp.e2e;

import nvt.kts.ticketapp.e2e.test.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SearchAndFilterTest.class,
        BuyingTest.class,
        ReservationTest.class,
        UserTicketsTest.class,
        CreateEventTest.class,
        EditEventTest.class,
        ConfirmReservationTest.class,
        EditLocationSchemeTest.class,
        EditProfileTest.class,
        LoginTest.class,
        RegistrationTest.class,
        ReportsTest.class
})
public class E2ETestSuite {
}
