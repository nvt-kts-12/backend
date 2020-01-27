package nvt.kts.ticketapp.controller.auth.authenticationController.login;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.model.user.Authority;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.domain.model.user.UserTokenState;
import nvt.kts.ticketapp.repository.user.AuthorityRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import nvt.kts.ticketapp.security.auth.JwtAuthenticationRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    private final String URL = "/api/auth/login";

    @Before
    public void setUp() {
        Authority roleRegistered = new Authority("ROLE_REGISTERED");
        authorityRepository.save(roleRegistered);

        User user = new User("user", "pass", "firstname", "lastname", "email@gmail.com");
        userRepository.save(user);
    }

    @Test
    public void testLogin_success() {
        JwtAuthenticationRequest request = new JwtAuthenticationRequest("user", "pass");

        ResponseEntity<UserTokenState> response = restTemplate.postForEntity(URL, request, UserTokenState.class);

        UserTokenState token = response.getBody();
        assertNotNull(token);
        assertNotNull(token.getAccessToken());
        assertNotEquals(token.getExpiresIn(), 0);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testLogin_wrongUsername() {
        JwtAuthenticationRequest request = new JwtAuthenticationRequest("invalidusername", "pass");

        ResponseEntity<UserTokenState> response = restTemplate.postForEntity(URL, request, UserTokenState.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody().getAccessToken());
        assertEquals(0, response.getBody().getExpiresIn());
    }

    @Test
    public void testLogin_wrongPassword() {
        JwtAuthenticationRequest request = new JwtAuthenticationRequest("user", "wrongpass");

        ResponseEntity<UserTokenState> response = restTemplate.postForEntity(URL, request, UserTokenState.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody().getAccessToken());
        assertEquals(0, response.getBody().getExpiresIn());
    }

    @Test
    public void testLogin_wrongUsernameAndPassword() {
        JwtAuthenticationRequest request = new JwtAuthenticationRequest("invaliduser", "wrongpass");

        ResponseEntity<UserTokenState> response = restTemplate.postForEntity(URL, request, UserTokenState.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody().getAccessToken());
        assertEquals(0, response.getBody().getExpiresIn());
    }
}
