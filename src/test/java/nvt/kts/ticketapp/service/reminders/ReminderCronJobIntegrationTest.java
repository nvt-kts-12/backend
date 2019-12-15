package nvt.kts.ticketapp.service.reminders;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:scheduler.properties")
public class ReminderCronJobIntegrationTest {

    @Autowired
    private Environment environment;

    @Test
    public void testReminders() {
        String expression = environment.getProperty("job.time-service.schedule");
        boolean isCronValid = CronSequenceGenerator.isValidExpression(expression);
        assertTrue(isCronValid);
    }
}
