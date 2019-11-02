package nvt.kts.ticketapp.domain.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class LocationDTO {

    private Long locationSchemeId;
    private List<LocationSectorsDTO> locationSectors;

    public LocationDTO(Long locationSchemeId, List<LocationSectorsDTO> locationSectors) {
        this.locationSchemeId = locationSchemeId;
        this.locationSectors = locationSectors;
    }
}
