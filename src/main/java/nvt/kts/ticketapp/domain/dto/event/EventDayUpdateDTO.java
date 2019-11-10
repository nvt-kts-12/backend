package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.model.event.EventDayState;

import java.util.Date;

@Data
@NoArgsConstructor
public class EventDayUpdateDTO {


    private Long id;

    private String date;
    private String reservationExpirationDate;
    private EventDayState eventDayState;


    }

