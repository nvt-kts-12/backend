package nvt.kts.ticketapp.domain.dto.location;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
public class LocationSchemeDTO {

    @NotNull
    @PositiveOrZero
    private Long id;

    private boolean deleted;

    @NotNull
    @NotEmpty(message="Location scheme name can't be empty.")
    private String name;

    private String address;

    public LocationSchemeDTO(boolean deleted, String name, String address) {
        this.deleted = deleted;
        this.name = name;
        this.address = address;
    }
}
