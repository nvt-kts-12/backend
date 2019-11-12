package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.event.EventDayState;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

@NoArgsConstructor
@Data
public class EventDayDTO {

    private Long id;
    @NotNull
    private String date;
    @NotNull
    private LocationDTO location;
    @NotNull
    private String reservationExpireDate;
    @NotNull
    private EventDayState eventDayState;


}
