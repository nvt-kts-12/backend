package nvt.kts.ticketapp.controller.ticket;

import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.dto.ticket.TicketsDTO;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.ticket.ReservationCanNotBeCancelled;
import nvt.kts.ticketapp.exception.ticket.TicketDoesNotExist;
import nvt.kts.ticketapp.exception.user.UserNotFound;
import nvt.kts.ticketapp.service.ticket.TicketService;
import nvt.kts.ticketapp.service.ticket.TicketServiceImpl;
import nvt.kts.ticketapp.service.user.UserService;
import nvt.kts.ticketapp.service.user.UserServiceImpl;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/tickets")
public class TicketController {

    private TicketService ticketService;
    private UserService userService;


    public TicketController(TicketServiceImpl ticketService, UserServiceImpl userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping("/reservations")
    private ResponseEntity getReservations(Principal user) {
        try {
            User u = userService.findByUsername(user.getName());
            return new ResponseEntity<TicketsDTO>(new TicketsDTO(ticketService.getReservationsFromUser(u)), HttpStatus.OK);
        } catch (UserNotFound unf) {
            unf.printStackTrace();
            return new ResponseEntity<String>(unf.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/sold")
    private ResponseEntity getSoldTickets(Principal user) {
        try {
            User u = userService.findByUsername(user.getName());
            return new ResponseEntity<TicketsDTO>(new TicketsDTO(ticketService.getSoldTicketsFromUser(u)), HttpStatus.OK);
        } catch (UserNotFound unf) {
            unf.printStackTrace();
            return new ResponseEntity<String>(unf.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity cancelReservation(Long ticketId) {
        try {
            return new ResponseEntity<TicketDTO>(ticketService.cancelReservation(ticketId), HttpStatus.OK);
        } catch (TicketDoesNotExist | ReservationCanNotBeCancelled ticketDoesNotExist) {
           ticketDoesNotExist.printStackTrace();
            return new ResponseEntity<String>(ticketDoesNotExist.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


}