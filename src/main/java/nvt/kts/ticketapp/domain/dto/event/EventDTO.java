package nvt.kts.ticketapp.domain.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class EventDTO {

    private Long id;
    @NotNull
    private String description;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private EventCategory category;

    private List<DateAndLocationDTO> dateAndLocationDTO;

    public EventDTO(Long id, String description, String name, EventCategory category) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.category = category;
    }

    public EventDTO(Event event) {
        this.id = event.getId();
        this.description = event.getDescription();
        this.category = event.getCategory();
        this.name = event.getName();
    }

}
