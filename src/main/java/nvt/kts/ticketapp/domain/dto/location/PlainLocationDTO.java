package nvt.kts.ticketapp.domain.dto.location;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
/**
 * This is location dto that does not contain location sectos
 */
public class PlainLocationDTO {

    @NotNull
    @PositiveOrZero
    private Long locationSchemeId;

    public PlainLocationDTO(Long locationSchemeId) {
        this.locationSchemeId = locationSchemeId;
    }

}
