package nvt.kts.ticketapp.domain.model.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class LocationSectors implements Serializable {

    @Id
    @ManyToOne
    private Sector sector;

    @Id
    @ManyToOne
    private Location location;

    @NotNull
    private int price;

    @NotNull
    private int capacity;

    @NotNull
    private boolean vip;

    public LocationSectors(Sector sector, Location location, @NotNull int price, @NotNull int capacity, @NotNull boolean vip) {
        this.sector = sector;
        this.location = location;
        this.price = price;
        this.capacity = capacity;
        this.vip = vip;
    }
}