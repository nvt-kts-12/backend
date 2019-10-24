package nvt.kts.TicketApp.domain.model.user;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Entity
@Table(name="admins")
public class Admin extends AbstractUser{

    public Admin(Long id, @NotNull String username, @NotNull String password,
                 @NotNull String firstName, @NotNull String lastName,
                 @NotNull UserRole userRole) {
        super(id, username, password, firstName, lastName, userRole);
    }
}
