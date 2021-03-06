package nvt.kts.ticketapp.domain.model.ticket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.AbstractEntity;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.location.SectorType;
import nvt.kts.ticketapp.domain.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ticket extends AbstractEntity {

    @Version
    private Long id;

    @NotNull
    private boolean sold;

    @NotNull
    private Long sectorId;

    @Enumerated(EnumType.STRING)
    private SectorType sectorType;

    @Column(name = "seat_row")
    private int seatRow;

    @Column(name = "seat_col")
    private int seatCol;

    @NotNull
    private double price;

    private boolean vip;

    @ManyToOne
    private EventDay eventDay;

    @ManyToOne
    private User user;

    public Ticket(@NotNull boolean sold,
                  @NotNull Long sectorId, int seatRow, int seatCol,
                  @NotNull double price, EventDay eventDay, User user, boolean vip) {
        this.sold = sold;
        this.sectorId = sectorId;
        this.seatRow = seatRow;
        this.seatCol = seatCol;
        this.price = price;
        this.eventDay = eventDay;
        this.user = user;
        this.vip = vip;
    }

    public Ticket(@NotNull boolean sold, @NotNull Long sectorId, @NotNull double price, boolean vip, EventDay eventDay, User user) {
        this.sold = sold;
        this.sectorId = sectorId;
        this.price = price;
        this.vip = vip;
        this.eventDay = eventDay;
        this.user = user;
    }
}