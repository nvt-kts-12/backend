package nvt.kts.TicketApp.domain.model.event;

import nvt.kts.TicketApp.domain.model.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Event extends AbstractEntity {

    @NotNull
    private String name;

    @NotNull
    private EventCategory category;

    @NotNull
    private String description;

    @OneToMany
    private List<EventDay> eventDays;

    public Event(Long id, @NotNull String name,
                 @NotNull EventCategory category,
                 @NotNull String description, List<EventDay> eventDays) {
        super(id);
        this.name = name;
        this.category = category;
        this.description = description;
        this.eventDays = eventDays;
    }
}