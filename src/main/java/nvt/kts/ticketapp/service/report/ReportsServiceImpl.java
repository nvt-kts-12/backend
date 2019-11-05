package nvt.kts.ticketapp.service.report;

import nvt.kts.ticketapp.domain.dto.report.EventTicketsReportDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportsServiceImpl implements ReportsService {

    private EventRepository eventRepository;
    private EventDaysRepository eventDaysRepository;

    public ReportsServiceImpl(EventRepository eventRepository, EventDaysRepository eventDaysRepository){
        this.eventRepository = eventRepository;
        this.eventDaysRepository = eventDaysRepository;
    }

    public EventTicketsReportDTO eventReport(Long id) throws EventNotFound{
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFound(id));

        List<EventDay> eventDays = eventDaysRepository.findAllByEventId(id);

        int numOfTickets = 0;
        int numOfReservations = 0;
        double totalIncome = 0.0;
        double avgPrice = 0.0;

        for (EventDay eventDay: eventDays) {
            // todo
        }

        return null;
    }
}
