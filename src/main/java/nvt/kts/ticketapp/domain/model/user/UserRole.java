package nvt.kts.ticketapp.domain.model.user;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ADMIN, REGISTERED;

    @Override
    public String getAuthority() {
        return name();
    }
}
