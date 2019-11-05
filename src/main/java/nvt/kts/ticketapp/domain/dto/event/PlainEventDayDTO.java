package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.dto.location.PlainLocationDTO;

@Data
@NoArgsConstructor
/**
 * This is event day dto that is referring to plain location dto.
 */
public class PlainEventDayDTO {

    private Long id;
    private String date;
    private PlainLocationDTO location;
    private String reservationExpirationDate;

    public PlainEventDayDTO(Long id, String date, PlainLocationDTO location, String reservationExpirationDate) {
        this.id = id;
        this.date = date;
        this.location = location;
        this.reservationExpirationDate = reservationExpirationDate;
    }

}
