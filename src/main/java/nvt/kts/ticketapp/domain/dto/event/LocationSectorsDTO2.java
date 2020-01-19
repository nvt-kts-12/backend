package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.domain.model.location.SectorType;

@Data
@NoArgsConstructor
public class LocationSectorsDTO2 {

    private SectorType sectorType;

    private Long sectorId;

    private double price;

    private boolean vip;

    public LocationSectorsDTO2(SectorType sectorType, Long sectorId, double price, boolean vip) {
        this.sectorType = sectorType;
        this.sectorId = sectorId;
        this.price = price;
        this.vip = vip;
    }
}
