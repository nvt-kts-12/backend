package nvt.kts.ticketapp.controller.event;


import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayUpdateDTO;
import nvt.kts.ticketapp.domain.dto.event.EventEventDaysDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.exception.date.DateCantBeInThePast;
import nvt.kts.ticketapp.exception.date.DateFormatIsNotValid;
import nvt.kts.ticketapp.exception.event.EventDaysListEmpty;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.exception.event.EventdayNotFound;
import nvt.kts.ticketapp.exception.location.LocationNotAvailableThatDate;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.exception.sector.SectorCapacityOverload;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.service.event.EventService;
import nvt.kts.ticketapp.exception.event.ReservationExpireDateInvalid;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping(path="api/event")
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

     @PostMapping()
     private ResponseEntity save (@RequestBody EventEventDaysDTO eventEventDaysDTO){
         Event event = null;
         try {
             event = eventService.save(eventEventDaysDTO);
         } catch (DateFormatIsNotValid | LocationSchemeDoesNotExist | SectorDoesNotExist | LocationNotAvailableThatDate | ParseException | EventDaysListEmpty | SectorCapacityOverload | DateCantBeInThePast | ReservationExpireDateInvalid ex) {
             ex.printStackTrace();
             return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
         }

         return new ResponseEntity<EventDTO>(ObjectMapperUtils.map(event, EventDTO.class), HttpStatus.OK);
     }

     @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
     private ResponseEntity<Page<Event>> show (Pageable pageable){

         return new ResponseEntity<Page<Event>>(eventService.findAll(pageable),HttpStatus.OK);
     }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable(value = "id") Long eventId,
                                           @RequestBody EventDTO eventDetails){
        EventDTO eventDTO = null;
        try {
            eventDTO = eventService.update(eventId,eventDetails);
            return new ResponseEntity<EventDTO>(eventDTO, HttpStatus.OK);
        } catch (EventNotFound eventNotFound) {
            eventNotFound.printStackTrace();
            return new ResponseEntity<String>(eventNotFound.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @PutMapping
    public ResponseEntity updateEventDay(@RequestBody EventDayUpdateDTO eventDayUpdateDTO){

        try {
            return new ResponseEntity<EventDayUpdateDTO>(eventService.updateEventDay(eventDayUpdateDTO), HttpStatus.OK);
        } catch (EventdayNotFound | DateFormatIsNotValid ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
