package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class EventDayForTicketDTO {

    private Date date;
    private String location;
    private Date reservationExpireDate;

    public EventDayForTicketDTO(Date date, String location, Date reservationExpireDate) {
        this.date = date;
        this.location = location;
        this.reservationExpireDate = reservationExpireDate;
    }
}