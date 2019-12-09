package nvt.kts.ticketapp.controller.auth.authenticationController.edit;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.user.UserDTO;
import nvt.kts.ticketapp.domain.dto.user.UserEditDTO;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class EditUserIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    private final String URL = "/api/user/edit-profile";

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
    public void editUserFirstName_success() {

        UserEditDTO userEditDTO = new UserEditDTO("testfirstnamechanged", null, null);

        HttpEntity<UserEditDTO> userEditDTOHttpEntity = new HttpEntity<UserEditDTO>(userEditDTO);

        ResponseEntity<UserDTO> userResponse = restTemplate.withBasicAuth("username", "password")
                .exchange(URL, HttpMethod.PUT,
                        userEditDTOHttpEntity, UserDTO.class);

        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertNotNull(userResponse.getBody());
        assertEquals(userEditDTO.getFirstName(), userResponse.getBody().getFirstName());
    }

    @Test
    public void editUserLastName_success() {

        UserEditDTO userEditDTO = new UserEditDTO(null, "testlastnamechanged", null);

        HttpEntity<UserEditDTO> userEditDTOHttpEntity = new HttpEntity<UserEditDTO>(userEditDTO);

        ResponseEntity<UserDTO> userResponse = restTemplate.withBasicAuth("username", "password")
                .exchange(URL, HttpMethod.PUT,
                        userEditDTOHttpEntity, UserDTO.class);

        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertNotNull(userResponse.getBody());
        assertEquals(userEditDTO.getLastName(), userResponse.getBody().getLastName());
    }

    @Test
    public void editUserEmail_success() {

        UserEditDTO userEditDTO = new UserEditDTO(null, null, "emailchanged@gmail.com");

        HttpEntity<UserEditDTO> userEditDTOHttpEntity = new HttpEntity<UserEditDTO>(userEditDTO);

        ResponseEntity<UserDTO> userResponse = restTemplate.withBasicAuth("username", "password")
                .exchange(URL, HttpMethod.PUT,
                        userEditDTOHttpEntity, UserDTO.class);

        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertNotNull(userResponse.getBody());
        assertEquals(userEditDTO.getEmail(), userResponse.getBody().getEmail());

    }

    @Test
    public void editUser_success() {

        UserEditDTO userEditDTO = new UserEditDTO("firstnamechanged", "lastnamechanged", "emailchanged@gmail.com");

        HttpEntity<UserEditDTO> userEditDTOHttpEntity = new HttpEntity<UserEditDTO>(userEditDTO);

        ResponseEntity<UserDTO> userResponse = restTemplate.withBasicAuth("username", "password")
                .exchange(URL, HttpMethod.PUT,
                        userEditDTOHttpEntity, UserDTO.class);

        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertNotNull(userResponse.getBody());
        assertEquals(userEditDTO.getFirstName(), userResponse.getBody().getFirstName());
        assertEquals(userEditDTO.getLastName(), userResponse.getBody().getLastName());
        assertEquals(userEditDTO.getEmail(), userResponse.getBody().getEmail());

    }

    @Test
    public void editUser_invalidFirstName() {

        UserEditDTO userEditDTO = new UserEditDTO("", null, null);

        HttpEntity<UserEditDTO> userEditDTOHttpEntity = new HttpEntity<UserEditDTO>(userEditDTO);

        ResponseEntity<String> errorResponse = restTemplate.withBasicAuth("username", "password")
                .exchange(URL, HttpMethod.PUT,
                        userEditDTOHttpEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
        assertEquals("First name not valid", errorResponse.getBody());
    }

    @Test
    public void editUser_invalidLastName() {

        UserEditDTO userEditDTO = new UserEditDTO(null, "", null);

        HttpEntity<UserEditDTO> userEditDTOHttpEntity = new HttpEntity<UserEditDTO>(userEditDTO);

        ResponseEntity<String> errorResponse = restTemplate.withBasicAuth("username", "password")
                .exchange(URL, HttpMethod.PUT,
                        userEditDTOHttpEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
        assertEquals("Last name not valid", errorResponse.getBody());

    }

    @Test
    public void editUser_invalidEmail() {

        UserEditDTO userEditDTO = new UserEditDTO(null, null, "someinvalidemail");

        HttpEntity<UserEditDTO> userEditDTOHttpEntity = new HttpEntity<UserEditDTO>(userEditDTO);

        ResponseEntity<String> errorResponse = restTemplate.withBasicAuth("username", "password")
                .exchange(URL, HttpMethod.PUT,
                        userEditDTOHttpEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
        assertEquals("Email not valid", errorResponse.getBody());

    }

}
