package nvt.kts.ticketapp.controller.auth;

import nvt.kts.ticketapp.domain.dto.user.UserDTO;
import nvt.kts.ticketapp.domain.dto.user.UserRegistrationDTO;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.*;
import nvt.kts.ticketapp.service.user.UserService;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        User user = null;
        try {
            user = userService.create(userRegistrationDTO);
        } catch (UsernameAlreadyExist | UsernameNotValid | PasswordNotValid | EmailNotValid | FirstNameNotValid | LastNameNotValid | EmailAlreadyExist ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<UserDTO>(ObjectMapperUtils.map(user, UserDTO.class), HttpStatus.OK);
    }

}
