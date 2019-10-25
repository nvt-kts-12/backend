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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "location_sectors_ids", joinColumns = @JoinColumn(name = "location_id"))
    @Column(name = "sectorsids")
    private List<Long> sectorsIds;
    
    public Location(Long id, @NotNull LocationScheme scheme, @NotNull List<Long> sectorsIds) {
        super(id);
        this.scheme = scheme;
        this.sectorsIds = sectorsIds;
    }
}
