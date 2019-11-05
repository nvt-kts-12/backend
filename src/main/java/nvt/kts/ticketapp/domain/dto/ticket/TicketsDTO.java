package nvt.kts.ticketapp.domain.dto.ticket;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TicketsDTO {

    private List<TicketDTO> tickets = new ArrayList<>();

    public TicketsDTO(List<Ticket> tickets) {
        for (Ticket ticket: tickets) {
            this.tickets.add(new TicketDTO(ticket));
        }
    }
}
