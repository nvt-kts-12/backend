package nvt.kts.ticketapp.repository.location;

import nvt.kts.ticketapp.domain.model.location.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectorRepository extends JpaRepository<Sector, Long> {
    List<Sector> findAllByLocationSchemeId(Long id);
}
