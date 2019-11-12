package nvt.kts.ticketapp.domain.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@Getter
@Setter
public class LocationSectorsDTO {

    @NotNull
    @PositiveOrZero
    private Long sectorId;

    @NotNull
    @PositiveOrZero
    private double price;

    @PositiveOrZero
    private int capacity;
    private boolean vip;

    public LocationSectorsDTO(Long sectorId, double price, int capacity, boolean vip) {
        this.sectorId = sectorId;
        this.price = price;
        this.capacity = capacity;
        this.vip = vip;
    }
}
