package nvt.kts.ticketapp.service.reminder;

import com.google.zxing.WriterException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ReminderService {

    void sendReminders() throws IOException, WriterException;
    
}
