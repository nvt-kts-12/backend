package nvt.kts.ticketapp.repository.event;

import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventDaysRepository extends JpaRepository<EventDay, Long> {
    List<EventDay> findAllByDate(Date date);

    @Query("select e from EventDay e where e.id=?1 and e.state =?2")
    Optional<EventDay> findOneByIdAndState(Long eventDayId, EventDayState state);
}
