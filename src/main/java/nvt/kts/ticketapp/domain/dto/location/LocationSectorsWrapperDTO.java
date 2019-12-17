package nvt.kts.ticketapp.domain.dto.location;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LocationSectorsWrapperDTO {
    private List<LocationSectorDTO> sectors;
}
