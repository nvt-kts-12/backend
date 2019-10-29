package nvt.kts.ticketapp.repository;

import nvt.kts.ticketapp.domain.model.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
