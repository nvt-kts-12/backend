package nvt.kts.ticketapp.domain.dto.location;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationSchemeDTO {

    private Long id;
    private boolean deleted;
    private String name;
    private String address;
}
