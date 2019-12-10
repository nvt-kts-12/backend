package nvt.kts.ticketapp.service.user;

import nvt.kts.ticketapp.service.user.create.CreateUserUnitTest;
import nvt.kts.ticketapp.service.user.edit.EditUserUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        CreateUserUnitTest.class,
        EditUserUnitTest.class
})
public class UserServiceUnitTestsSuite {
}
