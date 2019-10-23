package nvt.kts.TicketApp.domain.model.user;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
public class RegisteredUser extends User{

    public RegisteredUser(Long id, @NotNull String username, @NotNull String password, @NotNull String firstName, @NotNull String lastName, @NotNull UserRole role) {
        super(id, username, password, firstName, lastName, role);
    }
}
