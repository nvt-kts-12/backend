package nvt.kts.ticketapp.domain.dto.ticket;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayForTicketDTO;
import nvt.kts.ticketapp.domain.dto.user.UserDTO;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.SectorType;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
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
    private boolean sold;
    private SectorType sectorType;
    private EventDTO event;
    private EventDayForTicketDTO eventDay;
    private EventDayState eventDayState;
    private UserDTO user;

    public TicketDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.sectorId = ticket.getSectorId();
        this.seatRow = ticket.getSeatRow();
        this.seatCol = ticket.getSeatCol();
        this.vip = ticket.isVip();
        this.price = ticket.getPrice();
        this.sold = ticket.isSold();
        this.event = ObjectMapperUtils.map(ticket.getEventDay().getEvent(), EventDTO.class);
        EventDayForTicketDTO eventDayForTicketDTO = new EventDayForTicketDTO();
        eventDayForTicketDTO.setDate(ticket.getEventDay().getDate());
        eventDayForTicketDTO.setReservationExpireDate(ticket.getEventDay().getReservationExpirationDate());
        eventDayForTicketDTO.setLocation(ticket.getEventDay().getLocation().getScheme().getName());
        this.eventDay = eventDayForTicketDTO;
        this.eventDayState =ticket.getEventDay().getState();
        this.user = ObjectMapperUtils.map(ticket.getUser(), UserDTO.class);
        this.sectorType = ticket.getSectorType();
    }
}
