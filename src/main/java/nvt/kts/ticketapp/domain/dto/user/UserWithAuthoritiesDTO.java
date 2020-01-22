package nvt.kts.ticketapp.domain.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.user.Authority;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class UserWithAuthoritiesDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private List<Authority> authorities;

    public UserWithAuthoritiesDTO(Long id, String username, String password, String firstName, String lastName, String email,
                   List<Authority> authorities) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.authorities = authorities;
    }
}