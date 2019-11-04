package nvt.kts.ticketapp.controller.ticket;

import nvt.kts.ticketapp.domain.dto.ticket.TicketsDTO;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.UserNotFound;
import nvt.kts.ticketapp.service.ticket.TicketService;
import nvt.kts.ticketapp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

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
}