package nvt.kts.ticketapp.service.ticket;

import nvt.kts.ticketapp.domain.dto.event.SeatDTO;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.exception.ticket.SeatIsNotAvailable;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.ticket.TicketNotFoundOrAlreadyBought;
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
    public List<Ticket> getAvailableTicketsForEventDayAndSector(Long eventDayId, Long sectorId) {
        return ticketRepository.findAllByEventDayIdAndSectorIdAndUserIdNullAndDeletedFalse(eventDayId, sectorId);
    }

    @Override
    public Ticket getAvailableGrandstandTicketForEventDayAndSector(SeatDTO seatDTO, EventDay eventDay) throws SeatIsNotAvailable {
        Optional<Ticket> ticket = ticketRepository.findOneByEventDayIdAndSectorIdAndSeatRowAndSeatColAndUserIdNullAndDeletedFalse(eventDay.getId(), seatDTO.getSectorId(), seatDTO.getRow(), seatDTO.getCol());

        if (ticket.isEmpty()) {
            throw new SeatIsNotAvailable(seatDTO);
        }

        return ticket.get();

    }

    @Override
    public List<Ticket> saveAll(List<Ticket> tickets) {
        return ticketRepository.saveAll(tickets);
    }

    @Override
    public List<Ticket> getAvailableTickets(Long eventDayId) {

        return ticketRepository.findAllByEventDayIdAndUserIdNullAndDeletedFalse(eventDayId);

    }

    @Override
    public List<Ticket> getReservationsFromUser(User user) {
        return ticketRepository.findByUserIdAndSoldTrue(user.getId());
    }

    @Override
    public List<Ticket> getSoldTicketsFromUser(User user) {
        return ticketRepository.findByUserIdAndSoldFalse(user.getId());
    }

    @Override
    public Ticket buyTicket(Long id) throws TicketNotFoundOrAlreadyBought {
        Ticket ticket = ticketRepository.findOneByIdAndSoldFalse(id).orElseThrow(() -> new TicketNotFoundOrAlreadyBought(id));

        ticket.setSold(true);
        return ticketRepository.save(ticket);
    }
}

