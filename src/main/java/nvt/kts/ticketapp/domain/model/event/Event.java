package nvt.kts.ticketapp.domain.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event extends AbstractEntity {

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EventCategory category;

    @NotNull
    private String description;

    public Event(@NotNull String name,
                 @NotNull EventCategory category,
                 @NotNull String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }
}