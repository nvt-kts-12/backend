package nvt.kts.ticketapp.repository.location;

import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationSchemeRepository extends JpaRepository<LocationScheme, Long> {
}
