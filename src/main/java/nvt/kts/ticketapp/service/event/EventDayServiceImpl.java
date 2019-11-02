package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventDayServiceImpl implements EventDayService {

    private final EventDaysRepository eventDaysRepository;

    @Autowired
    public EventDayServiceImpl(EventDaysRepository eventDaysRepository) {
        this.eventDaysRepository = eventDaysRepository;
    }

    @Override
    public void saveAll(List<EventDay> eventDays) {
        eventDaysRepository.saveAll(eventDays);
    }

    @Override
    public List<EventDay> getAll() {
        return eventDaysRepository.findAll();
    }
}
