package nvt.kts.ticketapp.repository.event;

import nvt.kts.ticketapp.domain.model.event.EventDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventDaysRepository extends JpaRepository<EventDay, Long> {
    List<EventDay> findAllByDate(Date date);
}
