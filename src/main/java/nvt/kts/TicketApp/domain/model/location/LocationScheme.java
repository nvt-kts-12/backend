package nvt.kts.TicketApp.domain.model.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.TicketApp.domain.model.AbstractEntity;

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

    @OneToMany(mappedBy = "locationScheme")
    private List<Sector> sectors;

    public LocationScheme(Long id, @NotNull String name,
                          @NotNull String address, List<Sector> sectors) {
        super(id);
        this.name = name;
        this.address = address;
        this.sectors = sectors;
    }
}
