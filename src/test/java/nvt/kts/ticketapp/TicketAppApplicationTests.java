package nvt.kts.ticketapp;

import nvt.kts.ticketapp.controller.auth.AuthenticationControllerUnitSuite;
import nvt.kts.ticketapp.service.user.UserServiceUnitSuite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({
		AuthenticationControllerUnitSuite.class,
		UserServiceUnitSuite.class
})
public class TicketAppApplicationTests {

	@Test
	public void contextLoads() {
	}

}
