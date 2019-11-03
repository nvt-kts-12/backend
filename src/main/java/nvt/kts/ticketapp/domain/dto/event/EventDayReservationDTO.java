package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EventDayReservationDTO {

    private Long userId;
    private Long eventDayId;
    private List<ParterDTO> parters;
    private List<SeatDTO> seats;

}
