package nvt.kts.ticketapp.domain.dto.report;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
public class EventReportDTO {

    @NotNull
    private EventDTO eventDTO;
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

    public EventReportDTO(EventDTO eventDTO, int numOfTickets, int numOfReservations, double totalIncome, double avgPrice) {
        this.eventDTO = eventDTO;
        this.numOfTickets = numOfTickets;
        this.numOfReservations = numOfReservations;
        this.totalIncome = totalIncome;
        this.avgPrice = avgPrice;
    }
}
