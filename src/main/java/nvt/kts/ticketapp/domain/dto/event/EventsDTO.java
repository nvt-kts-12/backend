package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.model.event.Event;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class EventsDTO {

    private List<EventDTO> events = new ArrayList<>();

    private long numberOfElements;

    public EventsDTO(List<EventDTO> events, long numberOfElements) {
        this.events = events;
        this.numberOfElements = numberOfElements;
    }
}
