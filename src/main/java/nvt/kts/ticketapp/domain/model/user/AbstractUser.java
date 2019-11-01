package nvt.kts.ticketapp.domain.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy=TABLE_PER_CLASS)
public abstract class AbstractUser {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private UserRole userRole;

    public AbstractUser(@NotNull String username, @NotNull String password, @NotNull String firstName, @NotNull String lastName, @NotNull UserRole userRole) {
        this.username = username;

        // TODO encode password with BCryptPasswordEncoder
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
    }
}