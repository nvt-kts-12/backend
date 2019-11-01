package nvt.kts.ticketapp.controller.event;


import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.service.event.EventService;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path="api/event")
public class EventController {

     private EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

     @PostMapping()
     private ResponseEntity<EventDTO> save (@RequestBody EventDTO eventDTO){
           Event event = null;
           event = eventService.save(eventDTO);

         return new ResponseEntity<EventDTO>(ObjectMapperUtils.map(event, EventDTO.class), HttpStatus.OK);
     }

     @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
     private ResponseEntity<Page<Event>> show (Pageable pageable){

         return new ResponseEntity<Page<Event>>(eventService.findAll(pageable),HttpStatus.OK);
     }


    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> update(@PathVariable(value = "id") Long eventId,
                                           @RequestBody EventDTO eventDetails){

        Event event = eventService.findOne(eventId);
        event.setName(eventDetails.getName());
        event.setCategory(eventDetails.getCategory());
        event.setDescription(eventDetails.getDescription());

        final Event updatedEmployee = eventService.save(ObjectMapperUtils.map(event, EventDTO.class));
        return ResponseEntity.ok(eventDetails);
    }



}
