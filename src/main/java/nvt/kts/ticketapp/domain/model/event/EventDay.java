package nvt.kts.ticketapp.domain.model.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.AbstractEntity;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class EventDay extends AbstractEntity {

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;

    @OneToOne
    private Location location;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date reservationExpirationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EventDayState state;

    @ManyToOne
    private Event event;

    public EventDay(@NotNull Date date, Location location,
                    @NotNull Date reservationExpirationDate,
                    @NotNull EventDayState state, Event event) {
        this.date = date;
        this.location = location;
        this.reservationExpirationDate = reservationExpirationDate;
        this.state = state;
        this.event = event;
    }
}
