package nvt.kts.ticketapp.domain.dto.location;

import lombok.Data;
import nvt.kts.ticketapp.domain.model.location.SectorType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class SectorForDrawingDTO {

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


    @NotNull
    @PositiveOrZero
    private int numOfAvailablePlaces;

    @PositiveOrZero
    private double price;

    private SectorType type;

    public SectorForDrawingDTO() {
        super();
    }


    public SectorForDrawingDTO(boolean deleted, double topLeftX, double topLeftY, double bottomRightX,
                               double bottomRightY, int capacity, int rowNum, int colNum, double price, SectorType type, int numOfAvailablePlaces) {
        this.deleted = deleted;
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.bottomRightX = bottomRightX;
        this.bottomRightY = bottomRightY;
        this.capacity = capacity;
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.price = price;
        this.type = type;
        this.numOfAvailablePlaces = numOfAvailablePlaces;
    }
}
