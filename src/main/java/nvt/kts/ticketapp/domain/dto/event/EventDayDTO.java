package nvt.kts.ticketapp.domain.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EventDayDTO {

    private Long id;
    private String date;
    private LocationDTO location;
    private String reservationExpireDate;

    public EventDayDTO(Long id, String date, LocationDTO location, String reservationExpireDate) {
        this.id = id;
        this.date = date;
        this.location = location;
        this.reservationExpireDate = reservationExpireDate;
    }
}
