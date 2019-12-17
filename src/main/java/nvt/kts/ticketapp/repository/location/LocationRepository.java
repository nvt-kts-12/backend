package nvt.kts.ticketapp.repository.location;

import nvt.kts.ticketapp.domain.model.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllBySchemeIdAndDeletedFalse(Long id);

    Optional<Location> findByIdAndDeletedFalse(Long locationId);
}
