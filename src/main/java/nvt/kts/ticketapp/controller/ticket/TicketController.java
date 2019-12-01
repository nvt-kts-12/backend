package nvt.kts.ticketapp.controller.ticket;

import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.dto.ticket.TicketsDTO;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.ticket.ReservationCanNotBeCancelled;
import nvt.kts.ticketapp.exception.ticket.TicketDoesNotExist;
import nvt.kts.ticketapp.exception.ticket.TicketNotFoundOrAlreadyBought;
import nvt.kts.ticketapp.exception.user.UserNotFound;
import nvt.kts.ticketapp.service.ticket.TicketService;
import nvt.kts.ticketapp.service.ticket.TicketServiceImpl;
import nvt.kts.ticketapp.service.user.UserService;
import nvt.kts.ticketapp.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/ticket")
public class TicketController {

    private TicketService ticketService;

    public TicketController(TicketServiceImpl ticketService) {
        this.ticketService = ticketService;
    }

    @PutMapping("/buy/{id}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity buyTicket(@PathVariable Long id) {
        try {
            Ticket ticket = ticketService.buyTicket(id);
            return new ResponseEntity<TicketDTO>(new TicketDTO(ticket), HttpStatus.OK);
        } catch (TicketNotFoundOrAlreadyBought ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity cancelReservation(@PathVariable Long id) {
        try {
            return new ResponseEntity<TicketDTO>(ticketService.cancelReservation(id), HttpStatus.OK);
        } catch (TicketDoesNotExist | ReservationCanNotBeCancelled ticketDoesNotExist) {
            ticketDoesNotExist.printStackTrace();
            return new ResponseEntity<String>(ticketDoesNotExist.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}