package nvt.kts.ticketapp.domain.dto.location;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class LocationSchemeSectorsDTO {
    @NotNull
    private LocationSchemeDTO locationScheme;
    @NotNull
    private List<SectorDTO> sectors;

    public LocationSchemeSectorsDTO(List<SectorDTO> sectorDTOS, LocationSchemeDTO locationSchemeDTO) {
        this.locationScheme = locationSchemeDTO;
        this.sectors = sectorDTOS;
    }
}
