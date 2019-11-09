package nvt.kts.ticketapp.service.reminder;

import org.springframework.stereotype.Service;

@Service
public interface ReminderService {

    boolean sendReminders();
    
}
