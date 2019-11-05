package nvt.kts.ticketapp.repository.event;

import nvt.kts.ticketapp.domain.model.event.EventDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventDaysRepository extends JpaRepository<EventDay, Long> {
    List<EventDay> findAllByDate(Date date);

    List<EventDay> findAllByEventId(Long id);

    Optional<EventDay> findByIdAndDeletedFalse(Long evendDayId);

}
