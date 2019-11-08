package nvt.kts.ticketapp.repository.event;

import nvt.kts.ticketapp.domain.model.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    Optional<Event> findByIdAndDeletedFalse(Long aLong);
}


