package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.domain.dto.event.EventDayBuyingDTO;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExist;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExistOrStateIsNotValid;
import nvt.kts.ticketapp.exception.event.EventdayNotFound;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface EventDayService {
    void saveAll(List<EventDay> eventDays);

//    List<EventDay> findAllByDate(Date date);

    EventDay findOneById(Long eventDayId) throws EventDayDoesNotExist;

    EventDay getReservableAndBuyableAndDateAfter(Long eventDayId, Date date) throws EventDayDoesNotExistOrStateIsNotValid;

    EventDay getByIdAndDateAfter(Long eventDayId, Date date) throws EventdayNotFound;

    EventDay save(EventDay eventDay);

}
