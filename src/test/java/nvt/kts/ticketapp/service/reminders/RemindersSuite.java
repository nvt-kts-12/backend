package nvt.kts.ticketapp.service.reminders;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ReminderCronJobIntegrationTest.class,
        ReminderServiceIntegrationTest.class,
        ReminderServiceUnitTest.class
})
public class RemindersSuite {
}
