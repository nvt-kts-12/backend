package nvt.kts.ticketapp.controller.event;

import nvt.kts.ticketapp.domain.dto.event.*;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayUpdateDTO;
import nvt.kts.ticketapp.domain.dto.event.EventEventDaysDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayReservationDTO;
import nvt.kts.ticketapp.domain.dto.ticket.TicketsDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.date.DateCantBeInThePast;
import nvt.kts.ticketapp.exception.date.DateFormatIsNotValid;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExist;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExistOrStateIsNotValid;
import nvt.kts.ticketapp.exception.event.EventDaysListEmpty;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.exception.event.EventdayNotFound;
import nvt.kts.ticketapp.exception.location.LocationNotAvailableThatDate;
import nvt.kts.ticketapp.exception.location.LocationSectorsDoesNotExistForLocation;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.exception.sector.SectorCapacityOverload;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.exception.sector.SectorWrongType;
import nvt.kts.ticketapp.exception.ticket.NumberOfTicketsException;
import nvt.kts.ticketapp.exception.ticket.ReservationIsNotPossible;
import nvt.kts.ticketapp.exception.ticket.SeatIsNotAvailable;
import nvt.kts.ticketapp.repository.user.UserRepository;
import nvt.kts.ticketapp.service.event.EventService;
import nvt.kts.ticketapp.exception.event.ReservationExpireDateInvalid;
import nvt.kts.ticketapp.service.location.LocationService;
import nvt.kts.ticketapp.service.user.CustomUserDetailsService;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/event")
public class EventController {

    private EventService eventService;
    private CustomUserDetailsService customUserDetailsService;

    private LocationService locationService;

    // TODO delete when security is enabled
    @Autowired
    private UserRepository userRepository;


    public EventController(EventService eventService, CustomUserDetailsService customUserDetailsService) {
        this.eventService = eventService;
        this.customUserDetailsService = customUserDetailsService;
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
     private ResponseEntity<Page<Event>> show (Pageable pageable, @RequestParam(required=false) String searchQuery
             , @RequestParam(required=false) String dateFilter, @RequestParam(required=false) String typeFilter){
         return new ResponseEntity<Page<Event>>(eventService.findAll(pageable, searchQuery, dateFilter, typeFilter),HttpStatus.OK);
     }

     @PostMapping("/reserve")
     private ResponseEntity reserve(HttpServletRequest request, @RequestBody @Valid  EventDayReservationDTO eventDayReservationDTO) {

//        User user = customUserDetailsService.getUserFromRequest(request);

         // TODO delete when security is enabled
        Optional<User> user = userRepository.findOneByUsername("classicdocs");

        if (user.isEmpty()) {
            return new ResponseEntity<String>("Error", HttpStatus.BAD_REQUEST);
        }

        List<Ticket> tickets = null;
         try {
             tickets = eventService.reserve(eventDayReservationDTO, user.get());
         } catch (EventDayDoesNotExist | LocationSectorsDoesNotExistForLocation | SectorNotFound | SectorWrongType | EventDayDoesNotExistOrStateIsNotValid | NumberOfTicketsException | SeatIsNotAvailable | ReservationIsNotPossible ex) {
             ex.printStackTrace();
             return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
         } catch (ObjectOptimisticLockingFailureException e) {
             e.printStackTrace();
             return new ResponseEntity<String>("Something went wrong! Please try again.", HttpStatus.BAD_REQUEST);
         }

         return new ResponseEntity<TicketsDTO>(new TicketsDTO(tickets),HttpStatus.OK);
     }
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable(value = "id") Long eventId, @RequestBody EventDTO eventDetails){
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
