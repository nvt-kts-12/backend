package nvt.kts.ticketapp.service.report;

import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.LocationDTO;
import nvt.kts.ticketapp.domain.dto.event.PlainEventDayDTO;
import nvt.kts.ticketapp.domain.dto.location.PlainLocationDTO;
import nvt.kts.ticketapp.domain.dto.report.EventDayReportDTO;
import nvt.kts.ticketapp.domain.dto.report.EventReportDTO;
import nvt.kts.ticketapp.domain.dto.report.LocationReportDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.exception.location.LocationNotFound;
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
    private LocationRepository locationRepository;

    public ReportsServiceImpl(EventRepository eventRepository, EventDaysRepository eventDaysRepository,
                              TicketRepository ticketRepository, LocationSectorRepository locationSectorRepository,
                              LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.eventDaysRepository = eventDaysRepository;
        this.ticketRepository = ticketRepository;
        this.locationSectorRepository = locationSectorRepository;
        this.locationRepository = locationRepository;
    }


    public EventReportDTO eventReport(Long id) throws EventNotFound {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFound(id));

        List<EventDay> eventDays = eventDaysRepository.findAllByEventId(id);

        int numOfTickets = 0, numOfReservations = 0;
        double totalIncome = 0.0, avgPrice = 0.0;

        // going through every event day and calculating number of tickets, reservations and total income
        for (EventDay eventDay : eventDays) {
            List<Ticket> tickets = ticketRepository.findByEventDayIdAndSoldTrueAndUserNotNull(eventDay.getId());
            List<Ticket> reservations = ticketRepository.findByEventDayIdAndSoldFalseAndUserNotNull(eventDay.getId());

            numOfTickets += tickets.size();
            numOfReservations += reservations.size();

            totalIncome += sumIncome(tickets);
        }

        if (numOfTickets != 0) avgPrice = totalIncome / numOfTickets;

        EventDTO eventDTO = ObjectMapperUtils.map(event, EventDTO.class);
        return new EventReportDTO(eventDTO, numOfTickets, numOfReservations, totalIncome, avgPrice);
    }

    public List<EventDayReportDTO> eventDaysReport(Long id) throws EventNotFound {

        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFound(id));
        List<EventDay> eventDays = eventDaysRepository.findAllByEventId(id);

        int numOfTickets, numOfReservations = 0;
        double totalIncome, avgPrice = 0.0;

        List<EventDayReportDTO> reports = new ArrayList<>();

        // going through every event day and creating report for every single day
        for (EventDay eventDay : eventDays) {
            List<Ticket> tickets = ticketRepository.findByEventDayIdAndSoldTrueAndUserNotNull(eventDay.getId());
            List<Ticket> reservations = ticketRepository.findByEventDayIdAndSoldFalseAndUserNotNull(eventDay.getId());

            // map contains sector ids as keys, and number of sold tickets by sector as values
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

    public LocationReportDTO locationReport(Long id) throws LocationNotFound {

        Location location = locationRepository.findById(id).orElseThrow(() -> new LocationNotFound(id));
        PlainLocationDTO locationDTO = ObjectMapperUtils.map(location, PlainLocationDTO.class);

        List<EventDay> eventDays = eventDaysRepository.findAllByLocationId(id);

        double totalIncome = 0.0;
        Map<EventCategory, Double> incomeByCategory = new HashMap<>();
        Map<EventCategory, Integer> numOfEventDaysByCategory = new HashMap<>();

        // going through every event day and calculating incomes and events on this location
        for (EventDay eventDay : eventDays) {
            List<Ticket> tickets = ticketRepository.findByEventDayIdAndSoldTrueAndUserNotNull(eventDay.getId());
            double incomeForEventDay = sumIncome(tickets);
            totalIncome += incomeForEventDay;

            // increase or initialise income for event category
            incomeByCategory = mapIncome(incomeByCategory, eventDay.getEvent().getCategory(), incomeForEventDay);

            // increase or initialise number of event days for event category
            numOfEventDaysByCategory = mapDay(numOfEventDaysByCategory, eventDay.getEvent().getCategory());

        }
        return new LocationReportDTO(locationDTO, totalIncome, incomeByCategory, numOfEventDaysByCategory);
    }

    /**
     * increases or initialises income for event category
     *
     * @param incomeByCategory  -   map of categories and incomes
     * @param category          -   category of an event whose income we are calculating
     * @param incomeForEventDay -   income to be mapped
     * @return
     */
    private Map<EventCategory, Double> mapIncome(Map<EventCategory, Double> incomeByCategory, EventCategory category,
                                                 double incomeForEventDay) {
        if (incomeByCategory.containsKey(category)) {
            double currentIncome = incomeByCategory.get(category);
            incomeByCategory.put(category, currentIncome + incomeForEventDay);
        } else {
            incomeByCategory.put(category, incomeForEventDay);
        }
        return incomeByCategory;
    }

    /**
     * increases or initialises number of event days by event category
     *
     * @param numOfEventDaysByCategory -   map of categories and number of event days
     * @param category                 -   category of an event whose event day we are calculationg
     * @return
     */
    private Map<EventCategory, Integer> mapDay(Map<EventCategory, Integer> numOfEventDaysByCategory, EventCategory category) {
        if (numOfEventDaysByCategory.containsKey(category)) {
            int currentNumOfDays = numOfEventDaysByCategory.get(category);
            numOfEventDaysByCategory.put(category, currentNumOfDays + 1);
        } else {
            numOfEventDaysByCategory.put(category, 1);
        }
        return numOfEventDaysByCategory;
    }

    /**
     * Calculates number of tickets sold by single locationSector
     *
     * @param eventDay -   day whose sectors we are going through
     * @param tickets  -   list of tickts for that day of event
     * @return -   map of sector id : number of tickets for that sector
     */
    private Map<Long, Integer> calculateTicketsBySector(EventDay eventDay, List<Ticket> tickets) {
        List<LocationSector> locationSectors = locationSectorRepository.findAllByLocationIdAndDeletedFalse(eventDay.getLocation().getId());

        // map that is going to be returned
        Map<Long, Integer> sectorId_tickets = new HashMap<>();

        // map initialising
        for (LocationSector sector : locationSectors) {
            sectorId_tickets.put(sector.getId(), 0);
        }

        for (Ticket ticket : tickets) {
            int currentNumOfTickets = (int) sectorId_tickets.get(ticket.getSectorId());
            currentNumOfTickets++;
            sectorId_tickets.put(ticket.getSectorId(), currentNumOfTickets);
        }

        return sectorId_tickets;
    }

    /**
     * Calculates total sum of prices in ticket collection
     *
     * @param tickets -   list of tickets with prices
     * @return -   total sum
     */
    private double sumIncome(List<Ticket> tickets) {
        double retVal = 0.0;
        for (Ticket ticket : tickets) {
            retVal += ticket.getPrice();
        }
        return retVal;
    }
}
