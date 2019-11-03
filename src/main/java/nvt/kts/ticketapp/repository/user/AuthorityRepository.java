package nvt.kts.ticketapp.repository.user;

import nvt.kts.ticketapp.domain.model.user.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository  extends JpaRepository<Authority, Long> {
    Authority findOneById(Long id);
}
