package nvt.kts.ticketapp.domain.model.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.AbstractEntity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
    private boolean vip;

    @NotNull
    private double price;

    @NotNull
    private int capacity;

    @Column(name = "row_num")
    private int rowNum;

    @Column(name = "col_num")
    private int colNum;

    @NotNull
    private SectorType type;

    @ManyToOne
    private LocationScheme locationScheme;

    public Sector(Long id, @NotNull double topLeftX,
                  @NotNull double topLeftY, @NotNull double bottomRightX,
                  @NotNull double bottomRightY, @NotNull boolean vip,
                  @NotNull double price, @NotNull int capacity, int rowNum, int colNum,
                  @NotNull SectorType type, LocationScheme locationScheme) {
        super(id);
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.bottomRightX = bottomRightX;
        this.bottomRightY = bottomRightY;
        this.vip = vip;
        this.price = price;
        this.capacity = capacity;
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.type = type;
        this.locationScheme = locationScheme;
    }
}