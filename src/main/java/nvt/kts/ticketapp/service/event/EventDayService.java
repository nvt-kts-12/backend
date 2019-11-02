package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.domain.model.event.EventDay;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface EventDayService {
    void saveAll(List<EventDay> eventDays);

    List<EventDay> findAllByDate(Date date);
}
