package nvt.kts.ticketapp.service.ticket;

import com.google.zxing.WriterException;
import nvt.kts.ticketapp.domain.dto.event.SeatDTO;
import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.ticket.*;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.service.common.email.ticket.TicketEmailService;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketEmailService ticketEmailService;
    private final EventDaysRepository eventDaysRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, TicketEmailService ticketEmailService, EventDaysRepository eventDaysRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketEmailService = ticketEmailService;
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
    public List<Ticket> getReservationsFromUser(Long userId) {
        return ticketRepository.findByUserIdAndSoldFalse(userId);
    }

    @Override
    public List<Ticket> getBoughtTicketsFromUser(Long userId) {
        return ticketRepository.findByUserIdAndSoldTrue(userId);
    }

    @Override
    public Ticket buyTicket(Long id) throws TicketNotFoundOrAlreadyBought, IOException, WriterException, TicketListCantBeEmpty {
        Ticket ticket = ticketRepository.findOneByIdAndSoldFalse(id).orElseThrow(() -> new TicketNotFoundOrAlreadyBought(id));
        ticket.setSold(true);

        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);

        ticketEmailService.sendEmailForPurchasedTickets(ticket.getUser().getEmail(), tickets);

        return ticketRepository.save(ticket);
    }

    @Override
    public Optional<Ticket> getTicketById(Long id) throws TicketDoesNotExist {
        return ticketRepository.findOneById(id);
    }

    @Override
    public TicketDTO cancelReservation(Long ticketId) throws TicketDoesNotExist, ReservationCanNotBeCancelled {

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketDoesNotExist());
        EventDay ticketEventDay = ticket.getEventDay();
        User ticketUser = ticket.getUser();

        if (ticket.isSold() == false && ticketUser.getId() != null) {
            ticket.setUser(null);
        } else {
            throw new ReservationCanNotBeCancelled();
        }

        if (ticketEventDay.getState() == EventDayState.SOLD_OUT) {
            ticketEventDay.setState(EventDayState.RESERVABLE_AND_BUYABLE);
            eventDaysRepository.save(ticketEventDay);
        }

        ticketRepository.save(ticket);

        return ObjectMapperUtils.map(ticket, TicketDTO.class);
    }

    public List<TicketDTO> getAllTicketsForSectorAndEventDay(Long sectorId, Long eventDayId) {
        List<Ticket> allTickets = ticketRepository.findAllBySectorIdAndEventDayIdAndDeletedFalse(sectorId, eventDayId);
        return ObjectMapperUtils.mapAll(allTickets, TicketDTO.class);
    }
}

