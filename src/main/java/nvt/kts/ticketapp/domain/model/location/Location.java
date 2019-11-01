package nvt.kts.ticketapp.domain.model.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Location extends AbstractEntity {

    @OneToOne
    private LocationScheme scheme;

    public Location(@NotNull LocationScheme scheme, @NotNull List<Long> sectorsIds) {
        this.scheme = scheme;
    }

}
