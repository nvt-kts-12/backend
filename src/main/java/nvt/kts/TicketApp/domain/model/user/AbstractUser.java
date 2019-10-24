package nvt.kts.TicketApp.domain.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.TicketApp.domain.model.AbstractEntity;

import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.InheritanceType.SINGLE_TABLE;
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

    public AbstractUser(Long id, @NotNull String username,
                        @NotNull String password, @NotNull String firstName,
                        @NotNull String lastName, @NotNull UserRole userRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
    }
}