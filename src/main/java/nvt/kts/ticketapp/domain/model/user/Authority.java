package nvt.kts.ticketapp.domain.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nvt.kts.ticketapp.domain.model.AbstractEntity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Authority extends AbstractEntity implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    String name;

    public Authority(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}