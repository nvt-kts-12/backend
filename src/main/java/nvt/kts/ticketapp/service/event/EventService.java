package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public interface EventService  {

    Event save(EventDTO  eventDTO);
    Page<Event> findAll(Pageable pageable);
    Event findOne(Long eventId);

}
