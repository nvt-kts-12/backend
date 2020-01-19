package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.model.event.EventDayState;

import java.util.List;

@Data
@NoArgsConstructor
public class EventDayDTOHomePage {

    private Long id;
    private EventDTO event;
    private String date;
    private String reservationExpireDate;
    private EventDayState eventDayState;
    private Long locationSchemeId;
    private String locationName;
    private String locationAddress;
    private List<LocationSectorsDTO2> sectors;

    public EventDayDTOHomePage(Long id,EventDTO eventDTO, String date, String reservationExpireDate, EventDayState eventDayState, Long locationSchemeId, String locationName, String locationAddress, List<LocationSectorsDTO2> locationSectorsDTO2) {
        this.id = id;
        this.event = eventDTO;
        this.date = date;
        this.reservationExpireDate = reservationExpireDate;
        this.eventDayState = eventDayState;
        this.locationSchemeId = locationSchemeId;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.sectors = locationSectorsDTO2;
    }
}
