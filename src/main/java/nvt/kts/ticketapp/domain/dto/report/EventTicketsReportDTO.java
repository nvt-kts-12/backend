package nvt.kts.ticketapp.domain.dto.report;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;

@Data
@NoArgsConstructor
public class EventTicketsReportDTO {

    private EventDTO eventDTO;
    private int numOfTickets;
    private int numOfReservations;
    private double totalIncome;
    private double avgPrice;

    public EventTicketsReportDTO(EventDTO eventDTO, int numOfTickets, int numOfReservations, double totalIncome, double avgPrice) {
        this.eventDTO = eventDTO;
        this.numOfTickets = numOfTickets;
        this.numOfReservations = numOfReservations;
        this.totalIncome = totalIncome;
        this.avgPrice = avgPrice;
    }
}
