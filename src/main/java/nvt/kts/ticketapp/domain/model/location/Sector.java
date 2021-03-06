package nvt.kts.ticketapp.domain.model.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.AbstractEntity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Sector extends AbstractEntity {

    @NotNull
    private double topLeftX;

    @NotNull
    private double topLeftY;

    @NotNull
    private double bottomRightX;

    @NotNull
    private double bottomRightY;

    @NotNull
    private int capacity;

    @Column(name = "row_num")
    private int rowNum;

    @Column(name = "col_num")
    private int colNum;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private SectorType type;

    @ManyToOne
    private LocationScheme locationScheme;

    public Sector(@NotNull double topLeftX,
                  @NotNull double topLeftY, @NotNull double bottomRightX,
                  @NotNull double bottomRightY,
                  @NotNull int capacity, int rowNum, int colNum,
                  @NotNull SectorType type, LocationScheme locationScheme) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.bottomRightX = bottomRightX;
        this.bottomRightY = bottomRightY;
        this.capacity = capacity;
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.type = type;
        this.locationScheme = locationScheme;
    }
}