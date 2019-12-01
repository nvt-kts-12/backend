package nvt.kts.ticketapp.controller.auth;

import nvt.kts.ticketapp.domain.dto.ticket.TicketsDTO;
import nvt.kts.ticketapp.domain.dto.user.UserDTO;
import nvt.kts.ticketapp.domain.dto.user.UserEditDTO;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.EmailNotValid;
import nvt.kts.ticketapp.exception.user.FirstNameNotValid;
import nvt.kts.ticketapp.exception.user.LastNameNotValid;
import nvt.kts.ticketapp.exception.user.UserNotFound;
import nvt.kts.ticketapp.service.ticket.TicketService;
import nvt.kts.ticketapp.service.ticket.TicketServiceImpl;
import nvt.kts.ticketapp.service.user.UserService;
import nvt.kts.ticketapp.service.user.UserServiceImpl;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserService userService;
    private TicketService ticketService;

    public UserController(UserService userService, TicketService ticketService) {
        this.userService = userService;
        this.ticketService = ticketService;
    }


    @GetMapping("/me")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity user(Principal user) {
        try {
            User u = userService.findByUsername(user.getName());
            return new ResponseEntity<UserDTO>(ObjectMapperUtils.map(u, UserDTO.class), HttpStatus.OK);
        } catch (UserNotFound unf) {
            unf.printStackTrace();
            return new ResponseEntity<String>(unf.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/edit-profile")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity edit(Principal user, @RequestBody UserEditDTO userEditDTO) {
        try {
            User editedUser = userService.editUser(userEditDTO, userService.findByUsername(user.getName()));
            return new ResponseEntity<UserDTO>(ObjectMapperUtils.map(editedUser, UserDTO.class), HttpStatus.OK);
        } catch (UserNotFound | EmailNotValid | FirstNameNotValid | LastNameNotValid ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/reservations")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity getReservations(Principal user) {
        try {
            User u = userService.findByUsername(user.getName());
            return new ResponseEntity<TicketsDTO>(new TicketsDTO(ticketService.getReservationsFromUser(u)), HttpStatus.OK);
        } catch (UserNotFound unf) {
            unf.printStackTrace();
            return new ResponseEntity<String>(unf.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/bought")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity getBoughtTickets(Principal user) {
        try {
            User u = userService.findByUsername(user.getName());
            return new ResponseEntity<TicketsDTO>(new TicketsDTO(ticketService.getBoughtTicketsFromUser(u)), HttpStatus.OK);
        } catch (UserNotFound unf) {
            unf.printStackTrace();
            return new ResponseEntity<String>(unf.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}