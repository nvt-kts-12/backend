package nvt.kts.ticketapp.service.report;

import nvt.kts.ticketapp.domain.dto.report.EventDayReportDTO;
import nvt.kts.ticketapp.domain.dto.report.EventReportDTO;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReportsService {

    /**
     * Provides report about single event.
     * @param id    -   event id
     * @return  Number of tickets and reservations, average price of sold tickets and total income
     */
    public EventReportDTO eventReport(Long id) throws EventNotFound;

    /**
     * Provides report about event days in single event
     * @param id    -   id of an event whose days we are looking for
     * @return      -   list of reports that contain informations about number of tickets and reservations,
     *                  average price, total income and number of sold tickets by sector
     * @throws EventNotFound
     */
    public List<EventDayReportDTO> eventDaysReport(Long id) throws EventNotFound;
}
