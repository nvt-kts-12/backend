package nvt.kts.ticketapp.domain.dto.report;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayDTO;
import nvt.kts.ticketapp.domain.dto.event.LocationSectorsDTO;
import java.util.Map;

@Data
@NoArgsConstructor
public class EventDayReportDTO {

    private EventDayDTO eventDayDTO;
    private int numOfTickets;
    private int numOfReservations;
    private double totalIncome;
    private double avgPrice;
    private Map<LocationSectorsDTO, Integer> soldBySector;


}
