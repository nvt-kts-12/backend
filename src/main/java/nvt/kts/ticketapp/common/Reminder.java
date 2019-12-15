package nvt.kts.ticketapp.common;

import com.google.zxing.WriterException;
import nvt.kts.ticketapp.service.reminder.ReminderService;
import nvt.kts.ticketapp.service.reminder.ReminderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Reminder {

    private final ReminderService reminderService;

    public Reminder(ReminderServiceImpl reminderService) {
        this.reminderService = reminderService;
    }

    //this method is executed every day at 11:00:00
    @Scheduled(cron = "${job.time-service.schedule}")
    public void sendReminders() {
        try {
            reminderService.sendReminders();
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
    }
}
