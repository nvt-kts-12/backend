package nvt.kts.ticketapp.repository.event;

import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventDaysRepository extends JpaRepository<EventDay, Long> {
    List<EventDay> findAllByDate(Date date);

    Optional<EventDay> findOneByIdAndStateAndDateAfter(Long eventDayId, EventDayState state, Date date);

    List<EventDay> findAllByEventId(Long id);
    List<EventDay> findAllByLocationId(Long id);

    Optional<EventDay> findByIdAndDeletedFalse(Long evendDayId);

}
