package nvt.kts.ticketapp.repository.user;

import nvt.kts.ticketapp.domain.model.user.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findOneByUsername(String username);
}
