package nvt.kts.ticketapp.repository.user;

import nvt.kts.ticketapp.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByUsername(String username);
    Optional<User> findOneByEmail(String email);
}
