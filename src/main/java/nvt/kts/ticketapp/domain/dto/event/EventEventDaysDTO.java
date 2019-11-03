package nvt.kts.ticketapp.domain.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class EventEventDaysDTO {

    private EventDTO event;
    private List<EventDayDTO> eventDays;

    public EventEventDaysDTO(EventDTO event, List<EventDayDTO> eventDays) {
        this.event = event;
        this.eventDays = eventDays;
    }
}
