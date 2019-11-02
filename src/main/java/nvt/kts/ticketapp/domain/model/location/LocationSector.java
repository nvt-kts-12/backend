package nvt.kts.ticketapp.domain.model.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class LocationSector extends AbstractEntity {

    @ManyToOne
    private Sector sector;

    @ManyToOne
    private Location location;

    @NotNull
    private double price;

    @NotNull
    private int capacity;

    @NotNull
    private boolean vip;

    public LocationSector(Sector sector, Location location, @NotNull double price, @NotNull int capacity, @NotNull boolean vip) {
        super();
        this.sector = sector;
        this.location = location;
        this.price = price;
        this.capacity = capacity;
        this.vip = vip;
    }
}