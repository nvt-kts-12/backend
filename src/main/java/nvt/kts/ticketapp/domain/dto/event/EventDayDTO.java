package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.event.EventDayState;

import javax.validation.Valid;
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
    @Valid
    private LocationDTO location;
    @NotNull
    private String reservationExpireDate;

    private EventDayState eventDayState;

    public EventDayDTO(@NotNull String date, @NotNull LocationDTO location, @NotNull String reservationExpireDate, EventDayState eventDayState) {
        this.date = date;
        this.location = location;
        this.reservationExpireDate = reservationExpireDate;
        this.eventDayState = eventDayState;
    }

    public EventDayDTO(@NotNull String date, @NotNull LocationDTO location, @NotNull String reservationExpireDate) {
        this.date = date;
        this.location = location;
        this.reservationExpireDate = reservationExpireDate;
    }
}
