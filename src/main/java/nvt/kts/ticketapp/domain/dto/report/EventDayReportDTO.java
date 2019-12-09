package nvt.kts.ticketapp.domain.dto.report;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayDTO;
import nvt.kts.ticketapp.domain.dto.event.LocationSectorsDTO;
import nvt.kts.ticketapp.domain.dto.event.PlainEventDayDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;

@Data
@NoArgsConstructor
public class EventDayReportDTO {

    @NotNull
    private PlainEventDayDTO eventDayDTO;

    @NotNull
    @PositiveOrZero
    private int numOfTickets;
    @NotNull
    @PositiveOrZero
    private int numOfReservations;
    @NotNull
    @PositiveOrZero
    private double totalIncome;
    @NotNull
    @PositiveOrZero
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
