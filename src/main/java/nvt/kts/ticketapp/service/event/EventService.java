package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.domain.dto.event.EventEventDaysDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.exception.date.DateCantBeInPast;
import nvt.kts.ticketapp.exception.date.DateFormatNotValid;
import nvt.kts.ticketapp.exception.event.EventDaysListEmpty;
import nvt.kts.ticketapp.exception.location.LocationNotAvailableThatDate;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeNotExist;
import nvt.kts.ticketapp.exception.sector.SectorCapacityOverload;
import nvt.kts.ticketapp.exception.sector.SectorNotExist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;


@Service
public interface EventService  {

    Event save(EventEventDaysDTO eventEventDaysDTO) throws DateFormatNotValid, LocationSchemeNotExist, SectorNotExist, LocationNotAvailableThatDate, ParseException, EventDaysListEmpty, SectorCapacityOverload, DateCantBeInPast, ReservationExpireDateInvalid;
    Page<Event> findAll(Pageable pageable);
    Event findOne(Long eventId);

}
