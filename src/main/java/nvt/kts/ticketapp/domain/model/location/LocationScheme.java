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
public class LocationScheme extends AbstractEntity {

    @NotNull
    private String name;

    @NotNull
    private String address;

    public LocationScheme(@NotNull String name,
                          @NotNull String address) {
        this.name = name;
        this.address = address;
    }
}
