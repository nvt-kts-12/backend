package nvt.kts.ticketapp.controller.ticket;

import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.dto.ticket.TicketsDTO;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.ticket.TicketNotFoundOrAlreadyBought;
import nvt.kts.ticketapp.exception.user.UserNotFound;
import nvt.kts.ticketapp.service.ticket.TicketService;
import nvt.kts.ticketapp.service.ticket.TicketServiceImpl;
import nvt.kts.ticketapp.service.user.UserService;
import nvt.kts.ticketapp.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/ticket")
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

    @PostMapping("/buy/{id}")
    public ResponseEntity buyTicket(@PathVariable Long id) {
        try {
            Ticket ticket = ticketService.buyTicket(id);
            return new ResponseEntity<TicketDTO>(new TicketDTO(ticket), HttpStatus.OK);
        } catch (TicketNotFoundOrAlreadyBought ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}