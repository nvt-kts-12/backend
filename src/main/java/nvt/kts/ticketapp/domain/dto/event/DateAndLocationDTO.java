package nvt.kts.ticketapp.domain.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DateAndLocationDTO {

    private String date;
    private String location;

    public DateAndLocationDTO(String date, String location) {
        this.date = date;
        this.location = location;
    }
}
