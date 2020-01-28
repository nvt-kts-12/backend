package nvt.kts.ticketapp.domain.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatDTO {

    @NotNull
    @PositiveOrZero
    private Long sectorId;
    @NotNull
    @PositiveOrZero
    private int row;
    @NotNull
    @PositiveOrZero
    private int col;

}
