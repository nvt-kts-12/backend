package nvt.kts.ticketapp;

import nvt.kts.ticketapp.controller.auth.authenticationController.AuthenticationControllerUnitTestsSuite;
import nvt.kts.ticketapp.service.user.UserServiceUnitTestsSuite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({
		AuthenticationControllerUnitTestsSuite.class,
		UserServiceUnitTestsSuite.class
})
public class TicketAppApplicationTests {

	@Test
	public void contextLoads() {
	}

}
