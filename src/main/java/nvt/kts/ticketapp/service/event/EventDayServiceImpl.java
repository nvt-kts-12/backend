package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExist;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExistOrStateIsNotValid;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventDayServiceImpl implements EventDayService {

    private final EventDaysRepository eventDaysRepository;

    public EventDayServiceImpl(EventDaysRepository eventDaysRepository) {
        this.eventDaysRepository = eventDaysRepository;
    }

    @Override
    public void saveAll(List<EventDay> eventDays) {
        eventDaysRepository.saveAll(eventDays);
    }

    @Override
    public List<EventDay> findAllByDate(Date date) {
        return eventDaysRepository.findAllByDate(date);
    }

    @Override
    public EventDay findOneById(Long eventDayId) throws EventDayDoesNotExist {

        Optional<EventDay> eventDay = eventDaysRepository.findById(eventDayId);

        if (eventDay.isEmpty()) {
            throw new EventDayDoesNotExist();
        }

        return eventDay.get();

    }

    @Override
    public EventDay getReservableAndBuyableAndDateBefore(Long eventDayId, Date date) throws EventDayDoesNotExist, EventDayDoesNotExistOrStateIsNotValid {
        return eventDaysRepository.findOneByIdAndStateAndDateBefore(eventDayId, EventDayState.RESERVABLE_AND_BUYABLE,  date)
                .orElseThrow(() -> new EventDayDoesNotExistOrStateIsNotValid(eventDayId));

    }

    @Override
    public EventDay save(EventDay eventDay) {

        return eventDaysRepository.save(eventDay);
    }

}
