package nvt.kts.ticketapp.controller.auth.authenticationController;

import nvt.kts.ticketapp.controller.auth.authenticationController.edit.EditUserIntegrationTest;
import nvt.kts.ticketapp.controller.auth.authenticationController.login.LoginIntegrationTest;
import nvt.kts.ticketapp.controller.auth.authenticationController.register.RegisterIntegrationTest;
import nvt.kts.ticketapp.controller.auth.authenticationController.show.ShowUserIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RegisterIntegrationTest.class,
        EditUserIntegrationTest.class,
        ShowUserIntegrationTest.class,
        LoginIntegrationTest.class
})
public class AuthenticationControllerIntegrationTestsSuite {
}
