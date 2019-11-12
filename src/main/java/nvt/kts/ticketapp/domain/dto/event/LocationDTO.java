package nvt.kts.ticketapp.domain.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class LocationDTO {

    @NotNull
    private Long locationSchemeId;
    @NotNull
    private List<LocationSectorsDTO> locationSectors;

    public LocationDTO(Long locationSchemeId, List<LocationSectorsDTO> locationSectors) {
        this.locationSchemeId = locationSchemeId;
        this.locationSectors = locationSectors;
    }
}
