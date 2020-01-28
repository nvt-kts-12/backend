package nvt.kts.ticketapp.e2e.pages.home.helper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DateAndLocation {

    private String date;
    private String location;

    public DateAndLocation(String date, String location) {
        this.date = date;
        this.location = location;
    }
}
