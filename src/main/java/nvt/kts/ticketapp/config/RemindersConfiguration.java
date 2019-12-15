package nvt.kts.ticketapp.config;

import com.google.zxing.WriterException;
import nvt.kts.ticketapp.service.reminder.ReminderService;
import nvt.kts.ticketapp.service.reminder.ReminderServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Configuration
@EnableScheduling
@PropertySource("classpath:scheduler.properties")
public class RemindersConfiguration {

}
