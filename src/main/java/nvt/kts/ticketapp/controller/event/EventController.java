package nvt.kts.ticketapp.controller.event;


import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.EventEventDaysDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.exception.date.DateCantBeInPast;
import nvt.kts.ticketapp.exception.date.DateFormatNotValid;
import nvt.kts.ticketapp.exception.event.EventDaysListEmpty;
import nvt.kts.ticketapp.exception.location.LocationNotAvailableThatDate;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeNotExist;
import nvt.kts.ticketapp.exception.sector.SectorCapacityOverload;
import nvt.kts.ticketapp.exception.sector.SectorNotExist;
import nvt.kts.ticketapp.service.event.EventService;
import nvt.kts.ticketapp.service.event.ReservationExpireDateInvalid;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
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
     private ResponseEntity save (@RequestBody EventEventDaysDTO eventEventDaysDTO){
         Event event = null;
         try {
             event = eventService.save(eventEventDaysDTO);
         } catch (DateFormatNotValid | LocationSchemeNotExist | SectorNotExist | LocationNotAvailableThatDate | ParseException | EventDaysListEmpty | SectorCapacityOverload | DateCantBeInPast | ReservationExpireDateInvalid ex) {
             ex.printStackTrace();
             return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
         }

         return new ResponseEntity<EventDTO>(ObjectMapperUtils.map(event, EventDTO.class), HttpStatus.OK);
     }

     @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
     private ResponseEntity<Page<Event>> show (Pageable pageable){

         return new ResponseEntity<Page<Event>>(eventService.findAll(pageable),HttpStatus.OK);
     }
}
