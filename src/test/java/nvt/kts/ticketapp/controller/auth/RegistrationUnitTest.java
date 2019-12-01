package nvt.kts.ticketapp.controller.auth;

import nvt.kts.ticketapp.domain.dto.user.UserDTO;
import nvt.kts.ticketapp.domain.dto.user.UserRegistrationDTO;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.*;
import nvt.kts.ticketapp.service.user.UserService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationUnitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserService userServiceMocked;

    private static UserRegistrationDTO userRegistrationDTO;

    @BeforeClass
    public static void setUp() {
        userRegistrationDTO = new UserRegistrationDTO(null, "username", "password", "firstname", "lastname", "email@gmail.com");
    }

    @Test
    public void register_success() throws EmailNotValid, AuthorityDoesNotExist, EmailAlreadyExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {

        Mockito.when(userServiceMocked.create(Mockito.any(UserRegistrationDTO.class))).thenReturn( new User("username", "password", "firstname", "lastname", "email@gmail.com"));

        ResponseEntity<UserDTO> response = restTemplate.postForEntity("/api/auth/register", userRegistrationDTO, UserDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserDTO userDTO = (UserDTO) response.getBody();

        assertNotNull(userDTO);
        assertEquals("username", userDTO.getUsername());
        assertEquals("firstname", userDTO.getFirstName());
        assertEquals("lastname", userDTO.getLastName());
        assertEquals("email@gmail.com", userDTO.getEmail());

        Mockito.verify(userServiceMocked).create(Mockito.any(UserRegistrationDTO.class));
    }

    @Test
    public void register_EmailNotValid() throws  EmailNotValid, AuthorityDoesNotExist, EmailAlreadyExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {

        Mockito.when(userServiceMocked.create(Mockito.any(UserRegistrationDTO.class))).thenThrow(new EmailNotValid());

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", userRegistrationDTO, String.class );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email not valid", response.getBody());
        Mockito.verify(userServiceMocked).create(Mockito.any(UserRegistrationDTO.class));
    }

    @Test
    public void register_AuthorityDoesNotExist() throws  EmailNotValid, AuthorityDoesNotExist, EmailAlreadyExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {

        Mockito.when(userServiceMocked.create(Mockito.any(UserRegistrationDTO.class))).thenThrow(new AuthorityDoesNotExist(3L));

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", userRegistrationDTO, String.class );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Authority with id 3 doesn't exist", response.getBody());
        Mockito.verify(userServiceMocked).create(Mockito.any(UserRegistrationDTO.class));
    }

    @Test
    public void register_EmailAlreadyExist() throws  EmailNotValid, AuthorityDoesNotExist, EmailAlreadyExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {

        Mockito.when(userServiceMocked.create(Mockito.any(UserRegistrationDTO.class))).thenThrow(new EmailAlreadyExist());

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", userRegistrationDTO, String.class );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exist", response.getBody());
        Mockito.verify(userServiceMocked).create(Mockito.any(UserRegistrationDTO.class));
    }

    @Test
    public void register_UsernameNotValid() throws  EmailNotValid, AuthorityDoesNotExist, EmailAlreadyExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {

        Mockito.when(userServiceMocked.create(Mockito.any(UserRegistrationDTO.class))).thenThrow(new UsernameNotValid());

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", userRegistrationDTO, String.class );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username not valid", response.getBody());
        Mockito.verify(userServiceMocked).create(Mockito.any(UserRegistrationDTO.class));
    }

    @Test
    public void register_UsernameAlreadyExist() throws  EmailNotValid, AuthorityDoesNotExist, EmailAlreadyExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {

        Mockito.when(userServiceMocked.create(Mockito.any(UserRegistrationDTO.class))).thenThrow(new UsernameAlreadyExist());

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", userRegistrationDTO, String.class );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username already exist", response.getBody());
        Mockito.verify(userServiceMocked).create(Mockito.any(UserRegistrationDTO.class));
    }

    @Test
    public void register_PasswordNotValid() throws  EmailNotValid, AuthorityDoesNotExist, EmailAlreadyExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {

        Mockito.when(userServiceMocked.create(Mockito.any(UserRegistrationDTO.class))).thenThrow(new PasswordNotValid());

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", userRegistrationDTO, String.class );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Password not valid", response.getBody());
        Mockito.verify(userServiceMocked).create(Mockito.any(UserRegistrationDTO.class));
    }
}
