package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.model.event.EventDayState;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

@Data
@NoArgsConstructor
public class EventDayUpdateDTO {

    private Long id;
    @NotNull
    private String date;
    @NotNull
    private String reservationExpirationDate;
    @NotNull
    private EventDayState eventDayState;

    public EventDayUpdateDTO(Long id, @NotNull String date, @NotNull String reservationExpirationDate, @NotNull EventDayState eventDayState) {
        this.id = id;
        this.date = date;
        this.reservationExpirationDate = reservationExpirationDate;
        this.eventDayState = eventDayState;
    }
}

