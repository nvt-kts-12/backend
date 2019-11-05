package nvt.kts.ticketapp.service.ticket;

import nvt.kts.ticketapp.domain.dto.event.SeatDTO;
import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.exception.ticket.ReservationCanNotBeCancelled;
import nvt.kts.ticketapp.exception.ticket.SeatIsNotAvailable;
import nvt.kts.ticketapp.exception.ticket.TicketDoesNotExist;
import org.springframework.stereotype.Service;
import nvt.kts.ticketapp.domain.model.user.User;
import java.util.List;

@Service
public interface TicketService {
    List<Ticket> getAvailableTicketsForEventDayAndSector(Long eventDayId, Long sectorId);

    Ticket getAvailableGrandstandTicketForEventDayAndSector(SeatDTO seatDTO, EventDay eventDay) throws SeatIsNotAvailable;

    List<Ticket> saveAll(List<Ticket> tickets);

    List<Ticket> getAvailableTickets(Long id);

    List<Ticket> getReservationsFromUser(User user);

    List<Ticket> getSoldTicketsFromUser(User user);

    Ticket getTicketById(Long id) throws TicketDoesNotExist;

    TicketDTO cancelReservation (Long ticketId) throws TicketDoesNotExist, ReservationCanNotBeCancelled;
}
