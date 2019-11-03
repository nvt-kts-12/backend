package nvt.kts.ticketapp.domain.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LocationSectorsDTO {

    private Long sectorId;
    private double price;
    private int capacity;
    private boolean vip;

    public LocationSectorsDTO(Long sectorId, double price, int capacity, boolean vip) {
        this.sectorId = sectorId;
        this.price = price;
        this.capacity = capacity;
        this.vip = vip;
    }
}
