package nvt.kts.ticketapp.config;

import com.google.zxing.WriterException;
import nvt.kts.ticketapp.service.reminder.ReminderService;
import nvt.kts.ticketapp.service.reminder.ReminderServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Configuration
@EnableScheduling
public class RemindersConfiguration {

    private final ReminderService reminderService;

    public RemindersConfiguration(ReminderServiceImpl reminderService) {
        this.reminderService = reminderService;
    }

    //this method is executed every day at 11:00:00
    @Scheduled(cron = "0 0 11 * * ?")
    public void sendReminders() throws InterruptedException {
        try {
            reminderService.sendReminders();
        } catch (IOException|WriterException e) {
            e.printStackTrace();
        }
    }
}
