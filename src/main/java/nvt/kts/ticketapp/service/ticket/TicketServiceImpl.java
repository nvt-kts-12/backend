package nvt.kts.ticketapp.service.ticket;

import nvt.kts.ticketapp.domain.dto.event.SeatDTO;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.exception.ticket.SeatIsNotAvailable;
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
}
