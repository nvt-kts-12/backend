package nvt.kts.ticketapp.controller.report;

import nvt.kts.ticketapp.domain.dto.report.EventDayReportDTO;
import nvt.kts.ticketapp.domain.dto.report.EventTicketsReportDTO;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.service.report.ReportsService;
import nvt.kts.ticketapp.service.report.ReportsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private ReportsService reportsService;

    public ReportsController(ReportsServiceImpl reportsService){
        this.reportsService = reportsService;
    }

    @GetMapping("/event/{id}")
    public ResponseEntity eventReport(@PathVariable Long id){
        try {
            return new ResponseEntity<EventTicketsReportDTO>(reportsService.eventReport(id), HttpStatus.OK);
        } catch (EventNotFound eventNotFound) {
            eventNotFound.printStackTrace();
            return new ResponseEntity<String>(eventNotFound.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/eventDays/{id}")
    public ResponseEntity eventDaysReport(@PathVariable Long id){
        try {
            return new ResponseEntity<List<EventDayReportDTO>>(reportsService.eventDaysReport(id), HttpStatus.OK);
        } catch (EventNotFound eventNotFound) {
            eventNotFound.printStackTrace();
            return new ResponseEntity<String>(eventNotFound.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
