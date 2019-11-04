package nvt.kts.ticketapp.repository.sector;

import nvt.kts.ticketapp.domain.model.location.LocationSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationSectorRepository extends JpaRepository<LocationSector, Long> {
    List<LocationSector> findAllByLocationIdAndDeletedFalse(Long id);
}
