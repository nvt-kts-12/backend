package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.dto.location.PlainLocationDTO;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
/**
 * This is event day dto that is referring to plain location dto.
 */
public class PlainEventDayDTO {

    @NotNull
    @PositiveOrZero
    private Long id;

    @NotNull
    private String date;

    @NotNull
    private PlainLocationDTO location;

    @NotNull
    private String reservationExpirationDate;

    public PlainEventDayDTO(Long id, String date, PlainLocationDTO location, String reservationExpirationDate) {
        this.id = id;
        this.date = date;
        this.location = location;
        this.reservationExpirationDate = reservationExpirationDate;
    }

}
