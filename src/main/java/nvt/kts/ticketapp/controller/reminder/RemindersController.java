package nvt.kts.ticketapp.controller.reminder;

import nvt.kts.ticketapp.service.reminder.ReminderService;
import nvt.kts.ticketapp.service.reminder.ReminderServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reminders")
public class RemindersController {

    private final ReminderService reminderService;

    public RemindersController(ReminderServiceImpl reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping()
    public ResponseEntity sendReminders() {
        try {
            if (reminderService.sendReminders()) {
                return new ResponseEntity<String>("Reminders were sent successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("No reminders were sent", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
