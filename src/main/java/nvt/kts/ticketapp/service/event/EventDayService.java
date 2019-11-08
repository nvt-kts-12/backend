package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExist;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExistOrStateIsNotValid;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface EventDayService {
    void saveAll(List<EventDay> eventDays);

    List<EventDay> findAllByDate(Date date);

    EventDay findOneById(Long eventDayId) throws EventDayDoesNotExist;

    EventDay getReservableAndBuyableAndDateBefore(Long eventDayId, Date date) throws EventDayDoesNotExist, EventDayDoesNotExistOrStateIsNotValid;

    EventDay save(EventDay eventDay);
}
