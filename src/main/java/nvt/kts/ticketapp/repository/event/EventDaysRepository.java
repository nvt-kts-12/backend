package nvt.kts.ticketapp.repository.event;

import nvt.kts.ticketapp.domain.model.event.EventDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventDaysRepository extends JpaRepository<EventDay, Long> {
    List<EventDay> findAllByDate(Date date);

    @Query("select day from EventDay d where d.event.id = :id")
    List<EventDay>findAllByEventId(@Param("id") Long eventId);

    @Query("select day from EventDay d where d.event.id =:id and d.date= :date")
    EventDay findByEventIdAndDate(@Param("date") @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate, @Param("id") Long eventDayId);
}
