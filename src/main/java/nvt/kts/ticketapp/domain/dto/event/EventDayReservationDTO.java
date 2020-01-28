package nvt.kts.ticketapp.domain.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDayReservationDTO {

    @NotNull
    @PositiveOrZero
    private Long eventDayId;
    @NotNull
    private List<ParterDTO> parters;
    @NotNull
    private List<SeatDTO> seats;
    @NotNull
    private boolean purchase;

}
