package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.model.event.EventDayState;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class EventDayUpdateDTO {

    @NotNull
    private Long id;
    @NotNull
    private String date;
    @NotNull
    private String reservationExpirationDate;
    @NotNull
    private EventDayState eventDayState;


    }

