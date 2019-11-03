package nvt.kts.ticketapp.service.user;

import nvt.kts.ticketapp.domain.dto.user.UserRegistrationDTO;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User create(UserRegistrationDTO userRegistrationDTO) throws UsernameAlreadyExist, UsernameNotValid, PasswordNotValid, EmailNotValid, FirstNameNotValid, LastNameNotValid, EmailAlreadyExist;
    User findByUsername(String username);
}
