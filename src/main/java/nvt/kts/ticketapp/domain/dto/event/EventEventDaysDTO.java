package nvt.kts.ticketapp.domain.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class EventEventDaysDTO {

    @NotNull
    private EventDTO event;
    @NotNull
    private List<EventDayDTO> eventDays;

    public EventEventDaysDTO(EventDTO event, List<EventDayDTO> eventDays) {
        this.event = event;
        this.eventDays = eventDays;
    }
}
