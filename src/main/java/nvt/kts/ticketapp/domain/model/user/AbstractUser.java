package nvt.kts.ticketapp.domain.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.config.WebSecurityConfig;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    protected String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    public AbstractUser(@NotNull String username, @NotNull String password, @NotNull String firstName, @NotNull String lastName) {
        this.username = username;
        this.password = this.encodePassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private String encodePassword(String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}