package nvt.kts.ticketapp.domain.dto.report;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayDTO;
import nvt.kts.ticketapp.domain.dto.event.LocationSectorsDTO;
import nvt.kts.ticketapp.domain.dto.event.PlainEventDayDTO;

import java.util.Map;

@Data
@NoArgsConstructor
public class EventDayReportDTO {

    private PlainEventDayDTO eventDayDTO;
    private int numOfTickets;
    private int numOfReservations;
    private double totalIncome;
    private double avgPrice;
    private Map<Long, Integer> soldBySector;

    public EventDayReportDTO(PlainEventDayDTO eventDayDTO, int numOfTickets, int numOfReservations,
                             double totalIncome, double avgPrice, Map<Long, Integer> soldBySector) {
        this.eventDayDTO = eventDayDTO;
        this.numOfTickets = numOfTickets;
        this.numOfReservations = numOfReservations;
        this.totalIncome = totalIncome;
        this.avgPrice = avgPrice;
        this.soldBySector = soldBySector;
    }
}
