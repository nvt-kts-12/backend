package nvt.kts.ticketapp.controller.auth.authenticationController;

import nvt.kts.ticketapp.controller.auth.authenticationController.edit.EditUserUnitTest;
import nvt.kts.ticketapp.controller.auth.authenticationController.register.RegisterUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        RegisterUnitTest.class,
        EditUserUnitTest.class
})
public class AuthenticationControllerUnitTestsSuite {

}