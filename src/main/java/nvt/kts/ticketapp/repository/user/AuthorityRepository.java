package nvt.kts.ticketapp.repository.user;

import nvt.kts.ticketapp.domain.model.user.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository  extends JpaRepository<Authority, Long> {
    Optional<Authority> findOneById(Long id);

    Optional<Authority> findOneByName(String role_registered);
}
