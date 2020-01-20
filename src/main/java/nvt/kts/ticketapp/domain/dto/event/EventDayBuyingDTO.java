package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.event.EventDayState;

import java.util.List;

@Data
@NoArgsConstructor
public class EventDayBuyingDTO {

    private Long id;
    private EventDTO event;
    private String date;
    private String reservationExpireDate;
    private EventDayState eventDayState;
    private Long locationId;
    private String locationName;
    private String locationAddress;
    private List<SectorDTO> sectors;

    public EventDayBuyingDTO(Long id, EventDTO eventDTO, String date, String reservationExpireDate,
                             EventDayState eventDayState, Long locationId, String locationName,
                             String locationAddress, List<SectorDTO> sectors) {
        this.id = id;
        this.event = eventDTO;
        this.date = date;
        this.reservationExpireDate = reservationExpireDate;
        this.eventDayState = eventDayState;
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.sectors = sectors;
    }
}
