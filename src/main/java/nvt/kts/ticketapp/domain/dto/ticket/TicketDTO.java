package nvt.kts.ticketapp.domain.dto.ticket;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayForTicketDTO;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.util.ObjectMapperUtils;

@Data
@NoArgsConstructor
public class TicketDTO {

    private Long id;
    private Long sectorId;
    private int seatRow;
    private int seatCol;
    private boolean vip;
    private double price;
    private EventDTO event;
    private EventDayForTicketDTO eventDay;

    public TicketDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.sectorId = ticket.getSectorId();
        this.seatRow = ticket.getSeatRow();
        this.seatCol = ticket.getSeatCol();
        this.vip = ticket.isVip();
        this.price = ticket.getPrice();
        this.event = ObjectMapperUtils.map(ticket.getEventDay().getEvent(), EventDTO.class);
        EventDayForTicketDTO eventDayForTicketDTO = new EventDayForTicketDTO();
        eventDayForTicketDTO.setDate(ticket.getEventDay().getDate());
        eventDayForTicketDTO.setReservationExpireDate(ticket.getEventDay().getReservationExpirationDate());
        eventDayForTicketDTO.setLocation(ticket.getEventDay().getLocation().getScheme().getName());
    }
}