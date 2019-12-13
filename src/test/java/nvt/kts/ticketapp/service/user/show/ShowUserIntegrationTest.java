package nvt.kts.ticketapp.service.user.show;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.model.user.Authority;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.UserNotFound;
import nvt.kts.ticketapp.repository.user.AuthorityRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import nvt.kts.ticketapp.service.user.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShowUserIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Before
    public void setUp() {

        Authority roleRegistered = new Authority("ROLE_REGISTERED");
        authorityRepository.save(roleRegistered);

        User user = new User("username", "password", "firstname", "lastname",
                "email@gmail.com");
        userRepository.save(user);
        user.setAuthorities(new ArrayList<>(){{add(roleRegistered);}});
        userRepository.save(user);

    }

    @Test
    public void showUser_success() throws UserNotFound {

        String username = "username";

        User foundUser = userService.findByUsername(username);

        assertNotNull(foundUser);
        assertEquals(foundUser.getUsername(), "username");
        assertEquals(foundUser.getFirstName(), "firstname");
        assertEquals(foundUser.getLastName(), "lastname");
        assertEquals(foundUser.getEmail(), "email@gmail.com");
        assertTrue(new BCryptPasswordEncoder().matches("password",foundUser.getPassword()));
    }

    @Test(expected = UserNotFound.class)
    public void showUser_userNotFound() throws UserNotFound {

        String username = "nonexistentusername";

        userService.findByUsername(username);

    }
}
