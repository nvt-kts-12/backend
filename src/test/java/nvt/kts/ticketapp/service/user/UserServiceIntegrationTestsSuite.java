package nvt.kts.ticketapp.service.user;

import nvt.kts.ticketapp.service.user.create.CreateUserIntegrationTest;
import nvt.kts.ticketapp.service.user.edit.EditUserIntegrationTest;
import nvt.kts.ticketapp.service.user.show.ShowUserIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CreateUserIntegrationTest.class,
        EditUserIntegrationTest.class,
        ShowUserIntegrationTest.class
})
public class UserServiceIntegrationTestsSuite {
}
