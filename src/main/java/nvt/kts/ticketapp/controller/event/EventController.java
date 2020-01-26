package nvt.kts.ticketapp.controller.event;

import com.google.zxing.WriterException;
import nvt.kts.ticketapp.domain.dto.event.*;
import nvt.kts.ticketapp.domain.dto.ticket.TicketsDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.date.DateCantBeInThePast;
import nvt.kts.ticketapp.exception.date.DateFormatIsNotValid;
import nvt.kts.ticketapp.exception.event.*;
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
import nvt.kts.ticketapp.exception.ticket.TicketListCantBeEmpty;
import nvt.kts.ticketapp.exception.user.UserNotFound;
import nvt.kts.ticketapp.service.event.EventService;
import nvt.kts.ticketapp.service.location.LocationService;
import nvt.kts.ticketapp.service.user.UserService;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(path = "api/event")
public class EventController {

    private EventService eventService;
    private UserService userService;

    @Autowired
    private LocationService locationService;

    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity save(@RequestBody @Valid EventEventDaysDTO eventEventDaysDTO) {

        Event event = null;
        try {
            event = eventService.create(eventEventDaysDTO);
        } catch (DateFormatIsNotValid | LocationSchemeDoesNotExist | SectorDoesNotExist | LocationNotAvailableThatDate | EventDaysListEmpty | SectorCapacityOverload | DateCantBeInThePast | ReservationExpireDateInvalid | ParseException ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<EventDTO>(ObjectMapperUtils.map(event, EventDTO.class), HttpStatus.OK);
    }

    @GetMapping("/show-events")
    public ResponseEntity<EventsDTO> show(Pageable pageable, @RequestParam(required = false) String searchQuery,
                                          @RequestParam(required = false) String dateFilter,
                                          @RequestParam(required = false) String typeFilter,
                                          @RequestParam(required = false) String locationFilter) {

        return new ResponseEntity<EventsDTO>(eventService.findAll(pageable, searchQuery,
                dateFilter, typeFilter, locationFilter), HttpStatus.OK);
    }


    @PostMapping("/reserve")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity reserve(Principal user, @RequestBody @Valid EventDayReservationDTO eventDayReservationDTO) {

        List<Ticket> tickets = null;
        try {
            User u = (User) userService.findByUsername(user.getName());
            tickets = eventService.reserve(eventDayReservationDTO, u);
        } catch (LocationSectorsDoesNotExistForLocation | SectorNotFound | SectorWrongType | EventDayDoesNotExistOrStateIsNotValid | NumberOfTicketsException | SeatIsNotAvailable | ReservationIsNotPossible | UserNotFound ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ObjectOptimisticLockingFailureException | TicketListCantBeEmpty e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Something went wrong! Please try again.", HttpStatus.EXPECTATION_FAILED);
        } catch (IOException | WriterException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Could not generate QR code", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<TicketsDTO>(new TicketsDTO(tickets), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity update(@PathVariable(value = "id") Long eventId, @RequestBody @Valid EventDTO eventDetails) {
        EventDTO eventDTO = null;
        try {
            eventDTO = eventService.update(eventId, eventDetails);
            return new ResponseEntity<EventDTO>(eventDTO, HttpStatus.OK);
        } catch (EventNotFound eventNotFound) {
            eventNotFound.printStackTrace();
            return new ResponseEntity<String>(eventNotFound.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/eventDay/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateEventDay(@PathVariable Long id, @RequestBody @Valid EventDayUpdateDTO eventDayUpdateDTO) {

        try {
            return new ResponseEntity<EventDayUpdateDTO>(eventService.updateEventDay(id, eventDayUpdateDTO), HttpStatus.OK);
        } catch (EventdayNotFound | DateFormatIsNotValid ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getEventDays(@PathVariable Long id) {
        List<EventDayDTOHomePage> result = null;
        try {
            result = eventService.getEventDays(id);
        } catch (EventdayNotFound ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/eventDay/{id}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity getEventDay(@PathVariable Long id) {

        EventDayBuyingDTO result = null;
        try {
            result = eventService.getEventDay(id);
        } catch (EventdayNotFound ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity getAllEvents() {
        List<Event> result = null;
        try {
            result = eventService.findAllEvents();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
