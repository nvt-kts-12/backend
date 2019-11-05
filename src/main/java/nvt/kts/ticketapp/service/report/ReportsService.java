package nvt.kts.ticketapp.service.report;

import nvt.kts.ticketapp.domain.dto.report.EventTicketsReportDTO;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import org.springframework.stereotype.Service;

@Service
public interface ReportsService {

    /**
     * Provides report about single event.
     * @param id    -   event id
     * @return  Number of tickets and reservations, average price of sold tickets and total income
     */
    public EventTicketsReportDTO eventReport(Long id) throws EventNotFound;
}
