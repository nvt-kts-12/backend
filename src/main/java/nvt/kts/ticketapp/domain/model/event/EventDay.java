package nvt.kts.ticketapp.domain.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.AbstractEntity;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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

    @ManyToOne
    private Event event;

    public EventDay(Long id, @NotNull Date date, Location location,
                    @NotNull Date reservationExpirationDate,
                    @NotNull EventDayState state, List<Ticket> tickets, Event event) {
        super(id);
        this.date = date;
        this.location = location;
        this.reservationExpirationDate = reservationExpirationDate;
        this.state = state;
        this.event = event;
    }
}
