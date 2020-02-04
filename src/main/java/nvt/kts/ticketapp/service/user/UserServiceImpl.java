package nvt.kts.ticketapp.service.user;

import nvt.kts.ticketapp.domain.dto.user.UserEditDTO;
import nvt.kts.ticketapp.domain.dto.user.UserRegistrationDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.user.AbstractUser;
import nvt.kts.ticketapp.domain.model.user.Admin;
import nvt.kts.ticketapp.domain.model.user.Authority;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.*;
import nvt.kts.ticketapp.repository.user.AdminRepository;
import nvt.kts.ticketapp.repository.user.AuthorityRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nvt.kts.ticketapp.config.Constants.*;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private AdminRepository adminRepository;
    private AuthorityRepository authorityRepository;

    public UserServiceImpl(UserRepository userRepository, AdminRepository adminRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public User create(UserRegistrationDTO userRegistrationDTO) throws UsernameAlreadyExist, UsernameNotValid, PasswordNotValid, EmailNotValid, EmailAlreadyExist, AuthorityDoesNotExist {
        Optional<User> userFound = userRepository.findOneByUsername(userRegistrationDTO.getUsername());
        Optional<Admin> adminFound = adminRepository.findOneByUsername(userRegistrationDTO.getUsername());

        if (userFound.isPresent() || adminFound.isPresent()) {
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

        User newUser = new User(userRegistrationDTO.getUsername(),
                userRegistrationDTO.getPassword(),
                userRegistrationDTO.getFirstName(),
                userRegistrationDTO.getLastName(),
                userRegistrationDTO.getEmail());

        List<Authority> authorities = new ArrayList<Authority>();

        Optional<Authority> authority = authorityRepository.findOneByName("ROLE_REGISTERED");
        if (!authority.isPresent()) {
            throw new AuthorityDoesNotExist("ROLE_REGISTERED");
        }

        userRepository.save(newUser);

        authorities.add(authority.get());
        newUser.setAuthorities(authorities);

        return userRepository.save(newUser);
    }

    @Override
    public AbstractUser findByUsername(String username) throws UserNotFound{
        Optional<User> u = userRepository.findOneByUsername(username);
        if (u.isPresent()) {
            return u.get();
        } else {
            Optional<Admin> a = adminRepository.findOneByUsername(username);
            if (a.isPresent()) {
                return a.get();
            }
            throw new UserNotFound();
        }
    }

    @Override
    public User editUser(UserEditDTO userEditDTO, String username) throws UserNotFound, EmailNotValid, FirstNameNotValid, LastNameNotValid {

        Optional<User> userOptional = userRepository.findOneByUsername(username);

        if(!userOptional.isPresent()) {
            throw new UserNotFound();
        } else {
            User user = userOptional.get();
            if (userEditDTO.getEmail() != null) {
                if (!userEditDTO.getEmail().matches(EMAIL_REGEX)) {
                    throw new EmailNotValid();
                }
                user.setEmail(userEditDTO.getEmail());
            }

            if (userEditDTO.getFirstName() != null) {
                if (userEditDTO.getFirstName().matches(WHITESPACES_REGEX)) {
                    throw new FirstNameNotValid();
                }
                user.setFirstName(userEditDTO.getFirstName());
            }

            if (userEditDTO.getLastName() != null) {
                if (userEditDTO.getLastName().matches(WHITESPACES_REGEX)) {
                    throw new LastNameNotValid();
                }
                user.setLastName(userEditDTO.getLastName());
            }

            userRepository.save(user);
            return user;
        }

    }


}
