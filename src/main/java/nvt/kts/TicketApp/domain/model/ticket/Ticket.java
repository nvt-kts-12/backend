package nvt.kts.TicketApp.domain.model.ticket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.TicketApp.domain.model.AbstractEntity;
import nvt.kts.TicketApp.domain.model.event.EventDay;
import nvt.kts.TicketApp.domain.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ticket extends AbstractEntity {

    @NotNull
    private boolean sold;

    @NotNull
    private Long sectorId;

    @Column(name = "seat_row")
    private int seatRow;

    @Column(name = "seat_col")
    private int seatCol;

    @NotNull
    private double price;

    @ManyToOne
    private EventDay eventDay;

    @ManyToOne
    private User user;

    public Ticket(Long id, @NotNull boolean sold,
                  @NotNull Long sectorId, int seatRow, int seatCol,
                  @NotNull double price, EventDay eventDay, User user) {
        super(id);
        this.sold = sold;
        this.sectorId = sectorId;
        this.seatRow = seatRow;
        this.seatCol = seatCol;
        this.price = price;
        this.eventDay = eventDay;
        this.user = user;
    }
}