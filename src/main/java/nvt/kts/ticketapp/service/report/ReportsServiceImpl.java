package nvt.kts.ticketapp.service.report;

import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayDTO;
import nvt.kts.ticketapp.domain.dto.event.LocationSectorsDTO;
import nvt.kts.ticketapp.domain.dto.event.PlainEventDayDTO;
import nvt.kts.ticketapp.domain.dto.report.EventDayReportDTO;
import nvt.kts.ticketapp.domain.dto.report.EventTicketsReportDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportsServiceImpl implements ReportsService {

    private EventRepository eventRepository;
    private EventDaysRepository eventDaysRepository;
    private TicketRepository ticketRepository;
    private LocationSectorRepository locationSectorRepository;

    public ReportsServiceImpl(EventRepository eventRepository, EventDaysRepository eventDaysRepository,
                              TicketRepository ticketRepository, LocationSectorRepository locationSectorRepository){
        this.eventRepository = eventRepository;
        this.eventDaysRepository = eventDaysRepository;
        this.ticketRepository = ticketRepository;
        this.locationSectorRepository = locationSectorRepository;
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

    public List<EventDayReportDTO> eventDaysReport(Long id) throws EventNotFound {

        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFound(id));
        List<EventDay> eventDays = eventDaysRepository.findAllByEventId(id);

        int numOfTickets = 0;
        int numOfReservations = 0;
        double totalIncome = 0.0;
        double avgPrice = 0.0;

        List<EventDayReportDTO> reports = new ArrayList<>();

        for (EventDay eventDay: eventDays) {
            List<Ticket> tickets = ticketRepository.findByEventDayIdAndSoldTrueAndUserNotNull(eventDay.getId());
            List<Ticket> reservations = ticketRepository.findByEventDayIdAndSoldFalseAndUserNotNull(eventDay.getId());

            Map<Long, Integer> soldBySector = calculateTicketsBySector(eventDay, tickets);
            numOfTickets = tickets.size();
            numOfReservations = reservations.size();
            totalIncome = sumIncome(tickets);

            if (numOfTickets != 0) {
                avgPrice = totalIncome / numOfTickets;
            } else {
                avgPrice = 0.0;
            }

            reports.add(new EventDayReportDTO(ObjectMapperUtils.map(eventDay, PlainEventDayDTO.class), numOfTickets, numOfReservations,
                                                totalIncome, avgPrice, soldBySector));
        }

        return reports;
    }


    private Map<Long, Integer> calculateTicketsBySector(EventDay eventDay, List<Ticket> tickets){
        Location location = eventDay.getLocation();

        List<LocationSector> locationSectors = locationSectorRepository.findAllByLocationIdAndDeletedFalse(location.getId());
        Map<Long, Integer> sectorId_tickets = new HashMap<>();

        for (LocationSector sector: locationSectors) {
            sectorId_tickets.put(sector.getId(), 0);
        }

        for (Ticket ticket: tickets) {
            int currentNumOfTickets = (int) sectorId_tickets.get(ticket.getSectorId());
            currentNumOfTickets++;
            sectorId_tickets.put(ticket.getSectorId(), currentNumOfTickets);
        }

        return sectorId_tickets;
    }

    private double sumIncome(List<Ticket> tickets){
        int retVal = 0;
        for (Ticket ticket: tickets) {
            retVal += ticket.getPrice();
        }
        return retVal;
    }
}
