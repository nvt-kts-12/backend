package nvt.kts.ticketapp.service.ticket;

import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TicketService {
    List<Ticket> saveAll(List<Ticket> tickets);

    List<Ticket> getReservationsFromUser(User user);

    List<Ticket> getSoldTicketsFromUser(User user);
}