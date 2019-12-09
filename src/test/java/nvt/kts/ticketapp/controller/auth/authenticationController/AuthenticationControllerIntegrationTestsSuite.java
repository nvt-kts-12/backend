package nvt.kts.ticketapp.controller.auth.authenticationController;

import nvt.kts.ticketapp.controller.auth.authenticationController.register.RegisterIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RegisterIntegrationTest.class
})
public class AuthenticationControllerIntegrationTestsSuite {
}
