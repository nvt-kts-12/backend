package nvt.kts.ticketapp.service.user.create;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.user.UserRegistrationDTO;
import nvt.kts.ticketapp.domain.model.user.Admin;
import nvt.kts.ticketapp.domain.model.user.Authority;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.*;
import nvt.kts.ticketapp.repository.user.AdminRepository;
import nvt.kts.ticketapp.repository.user.AuthorityRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import nvt.kts.ticketapp.service.user.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateUserIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Before
    public void setUp() {
        Authority roleRegistered = new Authority("ROLE_REGISTERED");
        authorityRepository.save(roleRegistered);
        Authority roleAdmin = new Authority("ROLE_ADMIN");
        authorityRepository.save(roleAdmin);

        User user = new User("username", "Password123!", "firstname", "lastname", "email@gmail.com");
        userRepository.save(user);
        user.setAuthorities(new ArrayList<>(){{add(roleRegistered);}});
        userRepository.save(user);

        Admin admin = new Admin("admin", "Password123!", "firstname", "lastname");
        adminRepository.save(admin);
        admin.setAuthorities(new ArrayList<>(){{add(roleAdmin);}});
        adminRepository.save(admin);

    }

    @Test(expected = EmailAlreadyExist.class)
    public void create_EmailAlreadyExist() throws EmailAlreadyExist, EmailNotValid, AuthorityDoesNotExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(null, "username123", "Password123!", "firstname", "lastname", "email@gmail.com");

        userService.create(userRegistrationDTO);

        assertEquals(1, userRepository.findAll().size());

    }

    @Test(expected = EmailNotValid.class)
    public void create_EmailNotValid() throws EmailAlreadyExist, EmailNotValid, AuthorityDoesNotExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(null, "username123", "Password123!", "firstname", "lastname", "email@gmailcom");

        userService.create(userRegistrationDTO);

        assertEquals(1, userRepository.findAll().size());

    }

    @Test(expected = UsernameNotValid.class)
    public void create_UsernameNotValid() throws EmailAlreadyExist, EmailNotValid, AuthorityDoesNotExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(null, "12username", "Password123!", "firstname", "lastname", "email123@gmail.com");

        userService.create(userRegistrationDTO);

        assertEquals(1, userRepository.findAll().size());
    }

    @Test(expected = UsernameAlreadyExist.class)
    public void create_UsernameAlreadyExist_user() throws EmailAlreadyExist, EmailNotValid, AuthorityDoesNotExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(null, "username", "Password123!", "firstname", "lastname", "email@gmail.com");

        userService.create(userRegistrationDTO);

        assertEquals(1, userRepository.findAll().size());

    }

    @Test(expected = UsernameAlreadyExist.class)
    public void create_UsernameAlreadyExist_admin() throws EmailAlreadyExist, EmailNotValid, AuthorityDoesNotExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(null, "admin", "Password123!", "firstname", "lastname", "email@gmail.com");

        userService.create(userRegistrationDTO);

        assertEquals(1, userRepository.findAll().size());

    }

    @Test(expected = PasswordNotValid.class)
    public void create_PasswordNotValid() throws EmailAlreadyExist, EmailNotValid, AuthorityDoesNotExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(null, "username123", "123456!", "firstname", "lastname", "email123@gmail.com");

        userService.create(userRegistrationDTO);

        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    public void create_success() throws EmailAlreadyExist, EmailNotValid, AuthorityDoesNotExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(null, "username123", "Password123!", "firstname", "lastname", "email123@gmail.com");


        assertEquals(1, userRepository.findAll().size());

        User user = userService.create(userRegistrationDTO);

        assertNotNull(user);
        assertEquals(userRegistrationDTO.getUsername(), user.getUsername());
        assertTrue(new BCryptPasswordEncoder().matches(userRegistrationDTO.getPassword(),user.getPassword()));
        assertEquals(userRegistrationDTO.getFirstName(), user.getFirstName());
        assertEquals(userRegistrationDTO.getLastName(), user.getLastName());
        assertEquals(userRegistrationDTO.getEmail(), user.getEmail());
        assertEquals(1, user.getAuthorities().size());
        assertEquals("ROLE_REGISTERED", ((List<Authority>) user.getAuthorities()).get(0).getName());

        assertEquals(2, userRepository.findAll().size());
    }
}
