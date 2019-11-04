package nvt.kts.ticketapp.domain.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.event.EventDayState;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class EventDayDTO {

    private Long id;
    private Date date;
    private LocationDTO location;
    private Date reservationExpireDate;
    private EventDayState eventDayState;

    public EventDayDTO(Long id, Date date, LocationDTO location, Date reservationExpireDate,EventDayState eventDayState) {
        this.id = id;
        this.date = date;
        this.location = location;
        this.reservationExpireDate = reservationExpireDate;
        this.eventDayState = eventDayState;
    }
}
