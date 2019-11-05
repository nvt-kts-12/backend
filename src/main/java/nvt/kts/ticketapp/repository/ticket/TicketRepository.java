package nvt.kts.ticketapp.repository.ticket;
import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.dto.ticket.TicketsDTO;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByEventDayIdAndSectorIdAndUserIdNullAndDeletedFalse(Long eventDayId, Long sectorId);

    // check if seat is available for event day on sectorId
    Optional<Ticket> findOneByEventDayIdAndSectorIdAndSeatRowAndSeatColAndUserIdNullAndDeletedFalse(Long eventDay, Long sectorId, int row, int col);

    List<Ticket> findAllByEventDayIdAndUserIdNullAndDeletedFalse(Long eventDayId);

    List<Ticket> findByUserIdAndSoldTrue(Long userId);
    List<Ticket> findByUserIdAndSoldFalse(Long userId);

    List<Ticket> findByEventDayIdAndSoldTrueAndUserNotNull(Long id);

    List<Ticket> findByEventDayIdAndSoldFalseAndUserNotNull(Long id);

    //Optional<TicketDTO> findOneById(Long id);
}