package nvt.kts.ticketapp.domain.dto.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@Getter
@Setter
public class LocationSectorDTO {

    @NotNull
    private Long sectorId;

    @NotNull
    private Long locationId;

    @PositiveOrZero
    private double price;

    @Positive
    private int capacity;

    private boolean vip;

    public LocationSectorDTO(Long sectorId, Long locationId, double price, int capacity, boolean vip) {
        this.sectorId = sectorId;
        this.locationId = locationId;
        this.price = price;
        this.capacity = capacity;
        this.vip = vip;
    }
}
