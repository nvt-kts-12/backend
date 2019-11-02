package nvt.kts.ticketapp.domain.dto.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LocationSchemeSectorsDTO {
    private LocationScheme locationScheme;
    private List<SectorDTO> sectors;

    public LocationSchemeSectorsDTO(LocationScheme locationScheme, List<SectorDTO> sectors) {
        this.locationScheme = locationScheme;
        this.sectors = sectors;
    }
}
