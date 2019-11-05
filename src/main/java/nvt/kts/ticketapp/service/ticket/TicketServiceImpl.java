package nvt.kts.ticketapp.service.ticket;

import nvt.kts.ticketapp.domain.dto.event.SeatDTO;
import nvt.kts.ticketapp.domain.dto.report.EventReportDTO;
import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExist;
import nvt.kts.ticketapp.exception.ticket.ReservationCanNotBeCancelled;
import nvt.kts.ticketapp.exception.ticket.SeatIsNotAvailable;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.ticket.TicketDoesNotExist;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.hibernate.event.spi.ResolveNaturalIdEvent;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EventDaysRepository eventDaysRepository;

    public TicketServiceImpl(TicketRepository ticketRepository,EventDaysRepository eventDaysRepository) {
        this.ticketRepository = ticketRepository;
        this.eventDaysRepository = eventDaysRepository;
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
    public Ticket getTicketById(Long id) throws TicketDoesNotExist {
        return ticketRepository.getOne(id);
    }

    @Override
    public TicketDTO cancelReservation(Long ticketId) throws TicketDoesNotExist, ReservationCanNotBeCancelled {

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(()-> new TicketDoesNotExist(ticketId));
        EventDay ticketEventDay = ticket.getEventDay();
        User ticketUser = ticket.getUser();

        if(ticket.isSold() == false && ticketUser.getId() != null){
             ticket.setUser(null);
             ticketEventDay.setState(EventDayState.RESERVABLE_AND_BUYABLE);
        }
        else
        {
            throw new ReservationCanNotBeCancelled();
        }

        if(ticketEventDay.getState() == EventDayState.SOLD_OUT){
            ticketEventDay.setState(EventDayState.RESERVABLE_AND_BUYABLE);
        }

        ticketRepository.save(ticket);
        eventDaysRepository.save(ticketEventDay);

        return ObjectMapperUtils.map(ticket, TicketDTO.class);
    }
}

