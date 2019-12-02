package nvt.kts.ticketapp.domain.dto.location;

import lombok.Data;
import nvt.kts.ticketapp.domain.model.location.SectorType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class SectorDTO {

    @NotNull
    @PositiveOrZero
    private Long id;
    private boolean deleted;

    @NotNull
    @PositiveOrZero
    private double topLeftX;

    @NotNull
    @PositiveOrZero
    private double topLeftY;

    @NotNull
    @PositiveOrZero
    private double bottomRightX;
    @NotNull
    @PositiveOrZero
    private double bottomRightY;

    @NotNull
    @PositiveOrZero
    private int capacity;

    @NotNull
    @PositiveOrZero
    private int rowNum;
    @NotNull
    @PositiveOrZero
    private int colNum;


    private SectorType type;
}
