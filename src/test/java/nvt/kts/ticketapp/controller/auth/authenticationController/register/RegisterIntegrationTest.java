package nvt.kts.ticketapp.controller.auth.authenticationController.register;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.user.UserDTO;
import nvt.kts.ticketapp.domain.dto.user.UserRegistrationDTO;
import nvt.kts.ticketapp.domain.model.user.Admin;
import nvt.kts.ticketapp.domain.model.user.Authority;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.repository.user.AdminRepository;
import nvt.kts.ticketapp.repository.user.AuthorityRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriTemplateHandler;

import javax.annotation.PostConstruct;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class RegisterIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private TestRestTemplate clientTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    private String url = "/api/auth/register";

    @PostConstruct
    public void postConstruct() {
        this.clientTemplate = this.clientTemplate.withBasicAuth("username", "password");
    }

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

    @Test
    public void register_UsernameAlreadyExist() {

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(null, "username", "Password123!", "firstname", "lastname", "email123@gmail.com");
        ResponseEntity<String> response = clientTemplate.postForEntity(url, new HttpEntity<>(userRegistrationDTO), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username already exist", response.getBody());

    }

    @Test
    public void register_UsernameNotValid() {

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(null, "123username", "Password123!", "firstname", "lastname", "email123@gmail.com");
        ResponseEntity<String> response = clientTemplate.postForEntity(url, new HttpEntity<>(userRegistrationDTO), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username not valid", response.getBody());

    }

    @Test
    public void register_PasswordNotValid() {

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(null, "username123", "1234543!", "firstname", "lastname", "email123@gmail.com");
        ResponseEntity<String> response = clientTemplate.postForEntity(url, new HttpEntity<>(userRegistrationDTO), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Password not valid", response.getBody());

    }

    @Test
    public void register_EmailNotValid() {

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(null, "username123", "Password123!", "firstname", "lastname", "email123gmail.com");
        ResponseEntity<String> response = clientTemplate.postForEntity(url, new HttpEntity<>(userRegistrationDTO), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email not valid", response.getBody());

    }

    @Test
    public void register_EmailAlreadyExist() {

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(null, "username123", "Password123!", "firstname", "lastname", "email@gmail.com");
        ResponseEntity<String> response = clientTemplate.postForEntity(url, new HttpEntity<>(userRegistrationDTO), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exist", response.getBody());

    }

    @Test
    public void register_success() {

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(null, "username123", "Password123!", "firstname", "lastname", "email123@gmail.com");
        ResponseEntity<UserDTO> response = clientTemplate.postForEntity(url, new HttpEntity<>(userRegistrationDTO), UserDTO.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserDTO userDTO = response.getBody();
        assertEquals(userRegistrationDTO.getUsername(), userDTO.getUsername());
        assertEquals(userRegistrationDTO.getFirstName(), userDTO.getFirstName());
        assertEquals(userRegistrationDTO.getLastName(), userDTO.getLastName());
        assertEquals(userRegistrationDTO.getEmail(), userDTO.getEmail());
        assertNotNull(userDTO.getId());

    }
}
