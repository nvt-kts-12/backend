package nvt.kts.ticketapp.service.report;

import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.LocationSectorsDTO;
import nvt.kts.ticketapp.domain.dto.report.EventDayReportDTO;
import nvt.kts.ticketapp.domain.dto.report.EventTicketsReportDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportsServiceImpl implements ReportsService {

    private EventRepository eventRepository;
    private EventDaysRepository eventDaysRepository;
    private TicketRepository ticketRepository;

    public ReportsServiceImpl(EventRepository eventRepository, EventDaysRepository eventDaysRepository,
                              TicketRepository ticketRepository){
        this.eventRepository = eventRepository;
        this.eventDaysRepository = eventDaysRepository;
        this.ticketRepository = ticketRepository;
    }



    public EventTicketsReportDTO eventReport(Long id) throws EventNotFound{
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFound(id));

        List<EventDay> eventDays = eventDaysRepository.findAllByEventId(id);

        int numOfTickets = 0;
        int numOfReservations = 0;
        double totalIncome = 0.0;
        double avgPrice = 0.0;

        for (EventDay eventDay: eventDays) {
            List<Ticket> tickets = ticketRepository.findByEventDayIdAndSoldTrueAndUserNotNull(eventDay.getId());
            List<Ticket> reservations = ticketRepository.findByEventDayIdAndSoldFalseAndUserNotNull(eventDay.getId());

            numOfTickets += tickets.size();
            numOfReservations += reservations.size();

            totalIncome += sumIncome(tickets);
        }

        if(numOfTickets != 0)   avgPrice = totalIncome / numOfTickets;

        EventDTO eventDTO = ObjectMapperUtils.map(event, EventDTO.class);

        return new EventTicketsReportDTO(eventDTO, numOfTickets, numOfReservations, totalIncome, avgPrice);
    }

    public EventDayReportDTO eventDaysReport(Long id) throws EventNotFound {

        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFound(id));
        List<EventDay> eventDays = eventDaysRepository.findAllByEventId(id);

        int numOfTickets = 0;
        int numOfReservations = 0;
        double totalIncome = 0.0;
        double avgPrice = 0.0;
        Map<LocationSectorsDTO, Integer> soldBySector = new HashMap<>();

        for (EventDay eventDay: eventDays) {
            
        }

        return null;
    }


    private double sumIncome(List<Ticket> tickets){
        int retVal = 0;
        for (Ticket ticket: tickets) {
            retVal += ticket.getPrice();
        }
        return retVal;
    }
}
