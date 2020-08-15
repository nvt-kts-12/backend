package nvt.kts.ticketapp.controller.auth.authenticationController.edit;

import nvt.kts.ticketapp.domain.dto.user.UserDTO;
import nvt.kts.ticketapp.domain.dto.user.UserEditDTO;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.EmailNotValid;
import nvt.kts.ticketapp.exception.user.FirstNameNotValid;
import nvt.kts.ticketapp.exception.user.LastNameNotValid;
import nvt.kts.ticketapp.service.user.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class EditUserUnitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserServiceImpl userServiceMocked;

    @Test
    public void editUserFirstName_success() throws Exception {

        UserEditDTO userEditDTO = new UserEditDTO("testfirstnamechanged", null, null);

        User user = new User("username", "password",
                "firstname", "lastname", "email@gmail.com" );

        Mockito.when(userServiceMocked.editUser(Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()))).thenReturn(
                new User(user.getUsername(),
                        user.getPassword(),
                        userEditDTO.getFirstName(),
                        user.getLastName(),
                        user.getEmail()
                ));

        HttpEntity<UserEditDTO> userEditDTOHttpEntity = new HttpEntity<UserEditDTO>(userEditDTO);

        ResponseEntity<UserDTO> userResponse = restTemplate.withBasicAuth("username", "password")
                .exchange("/api/user/edit-profile", HttpMethod.PUT,
                userEditDTOHttpEntity, UserDTO.class);

        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertNotNull(userResponse.getBody());
        assertEquals(userEditDTO.getFirstName(), userResponse.getBody().getFirstName());
    }


    @Test
    public void editUserLastName_success() throws Exception {
        UserEditDTO userEditDTO = new UserEditDTO(null, "testlastnamechanged", null);

        User user = new User("username", "password",
                "firstname", "lastname", "email@gmail.com" );

        Mockito.when(userServiceMocked.editUser(Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()))).thenReturn(
                new User(user.getUsername(),
                        user.getPassword(),
                        user.getFirstName(),
                        userEditDTO.getLastName(),
                        user.getEmail()
                ));

        HttpEntity<UserEditDTO> userEditDTOHttpEntity = new HttpEntity<UserEditDTO>(userEditDTO);

        ResponseEntity<UserDTO> userResponse = restTemplate.withBasicAuth("username", "password")
                .exchange("/api/user/edit-profile", HttpMethod.PUT,
                        userEditDTOHttpEntity, UserDTO.class);

        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertNotNull(userResponse.getBody());
        assertEquals(userEditDTO.getLastName(), userResponse.getBody().getLastName());
    }

    @Test
    public void editUserEmail_success() throws Exception {
        UserEditDTO userEditDTO = new UserEditDTO(null, null, "emailchanged@gmail.com");

        User user = new User("username", "password",
                "firstname", "lastname", "email@gmail.com" );

        Mockito.when(userServiceMocked.editUser(Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()))).thenReturn(
                new User(user.getUsername(),
                        user.getPassword(),
                        user.getFirstName(),
                        user.getLastName(),
                        userEditDTO.getEmail()
                ));

        HttpEntity<UserEditDTO> userEditDTOHttpEntity = new HttpEntity<UserEditDTO>(userEditDTO);

        ResponseEntity<UserDTO> userResponse = restTemplate.withBasicAuth("username", "password")
                .exchange("/api/user/edit-profile", HttpMethod.PUT,
                        userEditDTOHttpEntity, UserDTO.class);

        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertNotNull(userResponse.getBody());
        assertEquals(userEditDTO.getEmail(), userResponse.getBody().getEmail());

    }

    @Test
    public void editUser_success() throws Exception {
        UserEditDTO userEditDTO = new UserEditDTO("firstnamechanged", "lastnamechanged", "emailchanged@gmail.com");

        User user = new User("username", "password",
                "firstname", "lastname", "email@gmail.com" );

        Mockito.when(userServiceMocked.editUser(Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()))).thenReturn(
                new User(user.getUsername(),
                        user.getPassword(),
                        userEditDTO.getFirstName(),
                        userEditDTO.getLastName(),
                        userEditDTO.getEmail()
                ));

        HttpEntity<UserEditDTO> userEditDTOHttpEntity = new HttpEntity<UserEditDTO>(userEditDTO);

        ResponseEntity<UserDTO> userResponse = restTemplate.withBasicAuth("username", "password")
                .exchange("/api/user/edit-profile", HttpMethod.PUT,
                        userEditDTOHttpEntity, UserDTO.class);

        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        assertNotNull(userResponse.getBody());
        assertEquals(userEditDTO.getFirstName(), userResponse.getBody().getFirstName());
        assertEquals(userEditDTO.getLastName(), userResponse.getBody().getLastName());
        assertEquals(userEditDTO.getEmail(), userResponse.getBody().getEmail());

    }

    @Test
    public void editUser_invalidFirstName() throws Exception {

        UserEditDTO userEditDTO = new UserEditDTO("", null, null);

        User user = new User("username", "password",
                "firstname", "lastname", "email@gmail.com" );

        Mockito.when(userServiceMocked.editUser(Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()))).thenThrow(new FirstNameNotValid());

        HttpEntity<UserEditDTO> userEditDTOHttpEntity = new HttpEntity<UserEditDTO>(userEditDTO);

        ResponseEntity<String> errorResponse = restTemplate.withBasicAuth("username", "password")
                .exchange("/api/user/edit-profile", HttpMethod.PUT,
                        userEditDTOHttpEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
        assertEquals("First name not valid", errorResponse.getBody());
    }

    @Test
    public void editUser_invalidLastName() throws Exception {

        UserEditDTO userEditDTO = new UserEditDTO(null, "", null);

        User user = new User("username", "password",
                "firstname", "lastname", "email@gmail.com" );

        Mockito.when(userServiceMocked.editUser(Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()))).thenThrow(new LastNameNotValid());

        HttpEntity<UserEditDTO> userEditDTOHttpEntity = new HttpEntity<UserEditDTO>(userEditDTO);

        ResponseEntity<String> errorResponse = restTemplate.withBasicAuth("username", "password")
                .exchange("/api/user/edit-profile", HttpMethod.PUT,
                        userEditDTOHttpEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
        assertEquals("Last name not valid", errorResponse.getBody());

    }

    @Test
    public void editUser_invalidEmail() throws Exception {

        UserEditDTO userEditDTO = new UserEditDTO(null, null, "someinvalidemail");

        User user = new User("username", "password",
                "firstname", "lastname", "email@gmail.com" );

        Mockito.when(userServiceMocked.editUser(Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()))).thenThrow(new EmailNotValid());

        HttpEntity<UserEditDTO> userEditDTOHttpEntity = new HttpEntity<UserEditDTO>(userEditDTO);

        ResponseEntity<String> errorResponse = restTemplate.withBasicAuth("username", "password")
                .exchange("/api/user/edit-profile", HttpMethod.PUT,
                        userEditDTOHttpEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
        assertEquals("Email not valid", errorResponse.getBody());

    }
}