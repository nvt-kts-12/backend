package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParterDTO {

    private Long sectorId;
    private int numberOfTickets;

}
