package nvt.kts.ticketapp.service.user;

import nvt.kts.ticketapp.domain.dto.user.UserRegistrationDTO;
import nvt.kts.ticketapp.domain.model.user.Authority;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.domain.model.user.UserRole;
import nvt.kts.ticketapp.exception.user.*;
import nvt.kts.ticketapp.repository.user.AuthorityRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nvt.kts.ticketapp.config.Constants.*;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(UserRegistrationDTO userRegistrationDTO) throws UsernameAlreadyExist, UsernameNotValid, PasswordNotValid, EmailNotValid, FirstNameNotValid, LastNameNotValid, EmailAlreadyExist {
        Optional<User> userFound = userRepository.findOneByUsername(userRegistrationDTO.getUsername());

        if (userFound.isPresent()) {
            throw new UsernameAlreadyExist();
        }

        userFound = userRepository.findOneByEmail(userRegistrationDTO.getEmail());

        if(userFound.isPresent()) {
            throw new EmailAlreadyExist();
        }

        if (!userRegistrationDTO.getUsername().matches(USERNAME_REGEX)) {
            throw new UsernameNotValid();
        }

        if (!userRegistrationDTO.getPassword().matches(PASSWORD_REGEX)) {
            throw new PasswordNotValid();
        }

        if (!userRegistrationDTO.getEmail().matches(EMAIL_REGEX)) {
            throw new EmailNotValid();
        }

        if (userRegistrationDTO.getFirstName().equals("")) {
            throw new FirstNameNotValid();
        }

        if (userRegistrationDTO.getLastName().equals("")) {
            throw new LastNameNotValid();
        }
        User newUser = new User(userRegistrationDTO.getUsername(),
                userRegistrationDTO.getPassword(),
                userRegistrationDTO.getFirstName(),
                userRegistrationDTO.getFirstName(),
                UserRole.REGISTERED,
                userRegistrationDTO.getEmail());

        List<Authority> authorities = new ArrayList<Authority>();
        authorities.add(authorityRepository.findOneById(1L));
        newUser.setAuthorities(authorities);

        return userRepository.save(newUser);
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> u = userRepository.findOneByUsername(username);
        return u.get();
    }
}
