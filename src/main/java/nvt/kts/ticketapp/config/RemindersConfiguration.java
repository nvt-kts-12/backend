package nvt.kts.ticketapp.config;

import nvt.kts.ticketapp.service.reminder.ReminderService;
import nvt.kts.ticketapp.service.reminder.ReminderServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class RemindersConfiguration {

    private final ReminderService reminderService;

    public RemindersConfiguration(ReminderServiceImpl reminderService) {
        this.reminderService = reminderService;
    }

    @Scheduled(cron = "0 0 11 * * ?")
    public void sendReminders() throws InterruptedException {
        reminderService.sendReminders();
    }
}
