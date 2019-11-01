package nvt.kts.ticketapp.domain.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.event.EventCategory;

@NoArgsConstructor
@Getter
@Setter
public class EventDTO {


    private Long id;
    private String description;
    private String name;
    private EventCategory category;

    public EventDTO(Long id, String description, String name, EventCategory category) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.category = category;
    }
}
