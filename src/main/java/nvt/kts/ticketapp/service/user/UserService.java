package nvt.kts.ticketapp.service.user;

import nvt.kts.ticketapp.domain.dto.user.UserEditDTO;
import nvt.kts.ticketapp.domain.dto.user.UserRegistrationDTO;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User create(UserRegistrationDTO userRegistrationDTO) throws UsernameAlreadyExist, UsernameNotValid, PasswordNotValid, EmailNotValid, EmailAlreadyExist, AuthorityDoesNotExist;
    User findByUsername(String username) throws UserNotFound;
    User editUser(UserEditDTO userEditDTO, User user) throws UserNotFound, EmailNotValid, FirstNameNotValid, LastNameNotValid;
}
