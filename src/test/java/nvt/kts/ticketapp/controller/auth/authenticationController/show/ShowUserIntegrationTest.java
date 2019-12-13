package nvt.kts.ticketapp.controller.auth.authenticationController.show;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.user.UserDTO;
import nvt.kts.ticketapp.domain.model.user.Authority;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.repository.user.AuthorityRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class ShowUserIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    private final String URL = "/api/user/me";

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
    public void showUser_success() {

        ResponseEntity<UserDTO> userResponse = restTemplate.withBasicAuth("username", "password")
                .getForEntity(URL, UserDTO.class);

        assertNotNull(userResponse);
        assertEquals(HttpStatus.OK, userResponse.getStatusCode());

        UserDTO userFromResponse = userResponse.getBody();

        assertNotNull(userFromResponse);
        assertEquals(userFromResponse.getUsername(), "username");
        assertEquals(userFromResponse.getFirstName(), "firstname");
        assertEquals(userFromResponse.getLastName(), "lastname");
        assertEquals(userFromResponse.getEmail(), "email@gmail.com");
    }
}
