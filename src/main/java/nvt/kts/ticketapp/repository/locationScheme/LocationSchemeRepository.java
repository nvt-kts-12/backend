package nvt.kts.ticketapp.repository.locationScheme;

import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationSchemeRepository extends JpaRepository<LocationScheme, Long> {

    Optional<LocationScheme> findOneById(Long id);
}
