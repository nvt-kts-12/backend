package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.event.EventDayState;

import java.util.Date;

@NoArgsConstructor
@Data
public class EventDayDTO {

    private Long id;
    private String date;
    private LocationDTO location;
    private String reservationExpireDate;
    private EventDayState eventDayState;



}
