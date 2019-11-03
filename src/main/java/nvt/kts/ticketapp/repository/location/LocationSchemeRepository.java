package nvt.kts.ticketapp.repository.location;

import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationSchemeRepository extends JpaRepository<LocationScheme, Long> {
    Optional<LocationScheme> findByNameIgnoreCase(String name);
}
