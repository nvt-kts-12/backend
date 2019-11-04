package nvt.kts.ticketapp.service.ticket;

import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Ticket> saveAll(List<Ticket> tickets) {
        return ticketRepository.saveAll(tickets);
    }

    @Override
    public List<Ticket> getReservationsFromUser(User user) {
        return ticketRepository.findByUserIdAndSold(user.getId(), false);
    }

    @Override
    public List<Ticket> getSoldTicketsFromUser(User user) {
        return ticketRepository.findByUserIdAndSold(user.getId(), true);
    }
}