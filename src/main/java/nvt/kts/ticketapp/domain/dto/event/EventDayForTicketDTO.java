package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.user.User;

import java.util.Date;

@Data
@NoArgsConstructor
public class EventDayForTicketDTO {

    private Date date;
    private String location;
    private Date reservationExpireDate;
    private EventDayState eventDayState;
    private User user;

    public EventDayForTicketDTO(Date date, String location, Date reservationExpireDate,EventDayState eventDayState,User user) {
        this.date = date;
        this.location = location;
        this.reservationExpireDate = reservationExpireDate;
        this.eventDayState = eventDayState;
        this.user = user;
    }
}
