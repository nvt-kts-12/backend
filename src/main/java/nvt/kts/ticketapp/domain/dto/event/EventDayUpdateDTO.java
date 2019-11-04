package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.model.event.EventDayState;

import java.util.Date;

@Data
@NoArgsConstructor
public class EventDayUpdateDTO {


    private Long id;

    private Date date;
    private Date reservationExpirationDate;
    private EventDayState eventDayState;

    public EventDayUpdateDTO(Long id, Date date, Date reservationExpirationDate, EventDayState eventDayState) {
        this.id = id;
        this.date = date;
        this.reservationExpirationDate = reservationExpirationDate;
        this.eventDayState = eventDayState;
    }
}
