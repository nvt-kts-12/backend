package nvt.kts.ticketapp.repository.event;

import nvt.kts.ticketapp.domain.model.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

interface CustomEventRepository {

    Page<Event> executeCustomQuery(Pageable pageable, String searchQuery,
                                   String dateFilter, String typeFilter, String locationFilter);
    
}
