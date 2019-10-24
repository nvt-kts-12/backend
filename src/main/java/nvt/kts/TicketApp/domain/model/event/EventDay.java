package nvt.kts.TicketApp.domain.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.TicketApp.domain.model.AbstractEntity;
import nvt.kts.TicketApp.domain.model.location.Location;
import nvt.kts.TicketApp.domain.model.ticket.Ticket;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class EventDay extends AbstractEntity {

    @NotNull
    private Date date;

    @OneToOne
    private Location location;

    @NotNull
    private Date reservationExpirationDate;

    @NotNull
    private EventDayState state;

    @OneToMany
    private List<Ticket> tickets;

    public EventDay(Long id, @NotNull Date date,
                    @NotNull Location location, @NotNull Date reservationExpirationDate,
                    @NotNull EventDayState state, List<Ticket> tickets) {
        super(id);
        this.date = date;
        this.location = location;
        this.reservationExpirationDate = reservationExpirationDate;
        this.state = state;
        this.tickets = tickets;
    }
}
