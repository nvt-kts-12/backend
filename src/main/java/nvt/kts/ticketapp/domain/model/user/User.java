package nvt.kts.ticketapp.domain.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="users")
public class User extends AbstractUser {

    @NotNull
    private String email;

    @NotNull
    private int bankAccount;

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets;

    public User(Long id, @NotNull String username, @NotNull String password,
                @NotNull String firstName, @NotNull String lastName, @NotNull UserRole userRole,
                @NotNull String email, @NotNull int bankAccount, List<Ticket> tickets) {
        super(id, username, password, firstName, lastName, userRole);
        this.email = email;
        this.bankAccount = bankAccount;
        this.tickets = tickets;
    }
}
