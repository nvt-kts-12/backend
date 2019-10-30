package nvt.kts.ticketapp.repository;

import nvt.kts.ticketapp.domain.model.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAll(Pageable pageable);
}
