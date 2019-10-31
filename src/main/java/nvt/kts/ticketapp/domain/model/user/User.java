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

    public User(@NotNull String username, @NotNull String password, @NotNull String firstName, @NotNull String lastName, @NotNull UserRole userRole, @NotNull String email) {
        super(username, password, firstName, lastName, userRole);
        this.email = email;
    }
}