package nvt.kts.ticketapp;

import nvt.kts.ticketapp.controller.ControllerSuite;
import nvt.kts.ticketapp.repository.RepositorySuite;
import nvt.kts.ticketapp.service.ServiceSuite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({
		ControllerSuite.class,
		ServiceSuite.class,
		RepositorySuite.class
})
public class TicketAppApplicationTests {

	@Test
	public void contextLoads() {
	}

}
