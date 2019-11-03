package nvt.kts.ticketapp.controller.auth;

import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/me")
    @PreAuthorize("hasRole('REGISTERED')")
    public User user(Principal user) {
        return this.userService.findByUsername(user.getName());
    }
}