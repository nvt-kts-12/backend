package nvt.kts.ticketapp.service.user;

import nvt.kts.ticketapp.service.user.create.CreateUserIntegrationTest;
import nvt.kts.ticketapp.service.user.edit.EditUserIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CreateUserIntegrationTest.class,
        EditUserIntegrationTest.class
})
public class UserServiceIntegrationTestsSuite {
}
