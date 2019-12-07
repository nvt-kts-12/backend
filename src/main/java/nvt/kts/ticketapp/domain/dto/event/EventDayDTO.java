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


    public EventDayDTO(Long id, @NotNull String date, @NotNull LocationDTO location, @NotNull String reservationExpireDate, @NotNull EventDayState eventDayState) {
        this.id = id;
        this.date = date;
        this.location = location;
        this.reservationExpireDate = reservationExpireDate;
        this.eventDayState = eventDayState;
    }
}
