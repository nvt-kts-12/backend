package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.model.event.Event;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class EventsDTO {

    private List<Event> events = new ArrayList<>();

    private int numberOfElements;

    public EventsDTO(List<Event> eventList, int numberOfElements) {
        this.events.addAll(eventList);
        this.numberOfElements = numberOfElements;
    }

}
