package nvt.kts.ticketapp.domain.model.user;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Entity
@Table(name="admins")
public class Admin extends AbstractUser{

    public Admin(@NotNull String username, @NotNull String password,
                 @NotNull String firstName, @NotNull String lastName) {
        super(username, password, firstName, lastName);
    }
}
