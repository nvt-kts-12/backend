package nvt.kts.ticketapp.domain.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
public class ParterDTO {

    @NotNull
    @PositiveOrZero
    private Long sectorId;
    @NotNull
    @Positive
    private int numberOfTickets;

}
