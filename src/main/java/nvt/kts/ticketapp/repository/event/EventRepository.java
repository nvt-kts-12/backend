package nvt.kts.ticketapp.repository.event;

import nvt.kts.ticketapp.domain.model.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event,Long>, CustomEventRepository {
    Optional<Event> findByIdAndDeletedFalse(Long aLong);
}


