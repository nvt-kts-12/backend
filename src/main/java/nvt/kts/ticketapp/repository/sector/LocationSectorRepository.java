package nvt.kts.ticketapp.repository.sector;

import nvt.kts.ticketapp.domain.model.location.LocationSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationSectorRepository extends JpaRepository<LocationSector, Long> {
    List<LocationSector> findAllByLocationIdAndDeletedFalse(Long id);

    List<LocationSector> findAllBySectorId(Long id);

}
