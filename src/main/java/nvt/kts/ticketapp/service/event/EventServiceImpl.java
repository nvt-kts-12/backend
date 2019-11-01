package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.repository.event.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {


    private EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    @Override
    public Event save(EventDTO eventDTO){
        return eventRepository.save(new Event(eventDTO.getId(), eventDTO.getName(),eventDTO.getCategory(),eventDTO.getDescription()));
    }

    @Override
    public Page<Event> findAll(Pageable pageable){
            return eventRepository.findAll(pageable);
    }

    @Override
    public Event findOne(Long id){return eventRepository.getOne(id);}


}
