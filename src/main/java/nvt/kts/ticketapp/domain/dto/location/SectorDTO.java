package nvt.kts.ticketapp.domain.dto.location;

import lombok.Data;
import nvt.kts.ticketapp.domain.model.location.SectorType;

@Data
public class SectorDTO {

    private Long id;
    private double topLeftX;
    private double topLeftY;

    private double bottomRightX;
    private double bottomRightY;

    private int capacity;

    private int rowNum;

    private int colNum;

    private SectorType type;
}
