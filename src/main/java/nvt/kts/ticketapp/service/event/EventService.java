package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.domain.dto.event.EventDayReservationDTO;
import nvt.kts.ticketapp.domain.dto.event.EventEventDaysDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.exception.date.DateCantBeInThePast;
import nvt.kts.ticketapp.exception.date.DateFormatIsNotValid;
import nvt.kts.ticketapp.exception.event.EventDaysListEmpty;
import nvt.kts.ticketapp.exception.event.ReservationExpireDateInvalid;
import nvt.kts.ticketapp.exception.location.LocationNotAvailableThatDate;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.exception.sector.SectorCapacityOverload;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;


@Service
public interface EventService  {

    Event save(EventEventDaysDTO eventEventDaysDTO) throws DateFormatIsNotValid, LocationSchemeDoesNotExist, SectorDoesNotExist, LocationNotAvailableThatDate, ParseException, EventDaysListEmpty, SectorCapacityOverload, DateCantBeInThePast, ReservationExpireDateInvalid;
    Page<Event> findAll(Pageable pageable);
    Event findOne(Long eventId);

    List<Ticket> reserve(EventDayReservationDTO eventDayReservationDTO) throws Exception;
}
