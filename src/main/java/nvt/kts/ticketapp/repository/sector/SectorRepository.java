package nvt.kts.ticketapp.repository.sector;

import nvt.kts.ticketapp.domain.model.location.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectorRepository  extends JpaRepository<Sector, Long> {
    Optional<Sector> findByIdAndDeletedFalse(Long id);

    List<Sector> findAllByDeletedFalse();

    List<Sector> findAllByLocationSchemeIdAndDeletedFalse(Long schemeId);
}
