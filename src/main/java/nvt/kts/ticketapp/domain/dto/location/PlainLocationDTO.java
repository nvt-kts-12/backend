package nvt.kts.ticketapp.domain.dto.location;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
/**
 * This is location dto that does not contain location sectos
 */
public class PlainLocationDTO {
    private Long locationSchemeId;

    public PlainLocationDTO(Long locationSchemeId) {
        this.locationSchemeId = locationSchemeId;
    }

}
