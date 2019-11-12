package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class EventDayForTicketDTO {

    @NotNull
    private Date date;
    @NotNull
    private String location;
    @NotNull
    private Date reservationExpireDate;

    public EventDayForTicketDTO(Date date, String location, Date reservationExpireDate) {
        this.date = date;
        this.location = location;
        this.reservationExpireDate = reservationExpireDate;
    }
}
