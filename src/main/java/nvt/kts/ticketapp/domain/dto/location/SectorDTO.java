package nvt.kts.ticketapp.domain.dto.location;

import lombok.Data;
import nvt.kts.ticketapp.domain.model.location.SectorType;

@Data
public class SectorDTO {

    private Long id;
    private boolean deleted;

    private double topLeftX;
    private double topLeftY;

    private double bottomRightX;
    private double bottomRightY;

    private int capacity;
    private int rowNum;
    private int colNum;

    private SectorType type;

    public SectorDTO() {}

    public SectorDTO(boolean deleted, double topLeftX, double topLeftY, double bottomRightX,
                     double bottomRightY, int capacity, int rowNum, int colNum, SectorType type) {
        this.deleted = deleted;
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.bottomRightX = bottomRightX;
        this.bottomRightY = bottomRightY;
        this.capacity = capacity;
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.type = type;
    }
}
