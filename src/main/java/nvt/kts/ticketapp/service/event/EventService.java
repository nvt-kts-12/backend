package nvt.kts.ticketapp.service.event;

import com.google.zxing.WriterException;
import nvt.kts.ticketapp.domain.dto.event.EventDayReservationDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayUpdateDTO;
import nvt.kts.ticketapp.domain.dto.event.EventEventDaysDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.date.DateCantBeInThePast;
import nvt.kts.ticketapp.exception.date.DateFormatIsNotValid;
import nvt.kts.ticketapp.exception.event.*;
import nvt.kts.ticketapp.exception.location.LocationNotAvailableThatDate;
import nvt.kts.ticketapp.exception.location.LocationSectorsDoesNotExistForLocation;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.exception.sector.SectorCapacityOverload;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.exception.sector.SectorWrongType;
import nvt.kts.ticketapp.exception.ticket.NumberOfTicketsException;
import nvt.kts.ticketapp.exception.ticket.ReservationIsNotPossible;
import nvt.kts.ticketapp.exception.ticket.SeatIsNotAvailable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


@Service
public interface EventService  {

    Event save(EventEventDaysDTO eventEventDaysDTO) throws DateFormatIsNotValid, LocationSchemeDoesNotExist, SectorDoesNotExist, LocationNotAvailableThatDate, ParseException, EventDaysListEmpty, SectorCapacityOverload, DateCantBeInThePast, ReservationExpireDateInvalid;
    Page<Event> findAll(Pageable pageable, String searchQuery, String dateFilter, String typeFilter, String locationFilter);
    Event findOne(Long eventId);
    EventDTO update(Long eventId,EventDTO eventDetails) throws EventNotFound;

    List<Ticket> reserve(EventDayReservationDTO eventDayReservationDTO, User user) throws EventDayDoesNotExist, LocationSectorsDoesNotExistForLocation, SectorNotFound, SectorWrongType, EventDayDoesNotExistOrStateIsNotValid, NumberOfTicketsException, SeatIsNotAvailable, ReservationIsNotPossible, IOException, WriterException;

    EventDayUpdateDTO updateEventDay(Long id, EventDayUpdateDTO eventDayDetails)throws EventdayNotFound,DateFormatIsNotValid;
    List<Event> findAllEvents();
}

