package nvt.kts.ticketapp.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nvt.kts.ticketapp.domain.dto.user.UserEditDTO;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.EmailNotValid;
import nvt.kts.ticketapp.exception.user.FirstNameNotValid;
import nvt.kts.ticketapp.exception.user.LastNameNotValid;
import nvt.kts.ticketapp.exception.user.UserNotFound;
import nvt.kts.ticketapp.service.user.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.security.Principal;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EditUserControllerUnitTest {

    @MockBean
    private UserServiceImpl userServiceMocked;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("username");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/user/edit-profile")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
                .content(objectMapper.writeValueAsString(userEditDTO)))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        UserEditDTO responseDTO = objectMapper.readValue(response.getContentAsString(), UserEditDTO.class);

        assertEquals(userEditDTO.getFirstName(), responseDTO.getFirstName());
        assertEquals(user.getLastName(), responseDTO.getLastName());
        assertEquals(user.getEmail(), responseDTO.getEmail());

        Mockito.verify(userServiceMocked, Mockito.times(1)).editUser(
                Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()));
    }

//    private String getUserEditInJson(UserEditDTO userEditDTO) {
//        return "{ \"firstName\" : " + (userEditDTO.getFirstName() != null ? ("\"" + userEditDTO.getFirstName() + "\"") : "null") +
//                ", \"lastName\" : " + (userEditDTO.getLastName() != null ? ("\"" + userEditDTO.getLastName() + "\"") : "null") +
//                ", \"email\": " + (userEditDTO.getEmail() != null ? ("\"" + userEditDTO.getEmail() + "\"") : "null") + "}";
//    }

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

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("username");


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/user/edit-profile")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
                .content(objectMapper.writeValueAsString(userEditDTO)))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        UserEditDTO responseDTO = objectMapper.readValue(response.getContentAsString(), UserEditDTO.class);

        assertEquals(userEditDTO.getLastName(), responseDTO.getLastName());
        assertEquals(user.getFirstName(), responseDTO.getFirstName());
        assertEquals(user.getEmail(), responseDTO.getEmail());

        Mockito.verify(userServiceMocked, Mockito.times(1)).editUser(
                Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()));
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

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("username");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/user/edit-profile")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
                .content(objectMapper.writeValueAsString(userEditDTO)))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        UserEditDTO responseDTO = objectMapper.readValue(response.getContentAsString(), UserEditDTO.class);

        assertEquals(user.getLastName(), responseDTO.getLastName());
        assertEquals(user.getFirstName(), responseDTO.getFirstName());
        assertEquals(userEditDTO.getEmail(), responseDTO.getEmail());

        Mockito.verify(userServiceMocked, Mockito.times(1)).editUser(
                Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()));
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

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("username");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/user/edit-profile")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
                .content(objectMapper.writeValueAsString(userEditDTO)))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        UserEditDTO responseDTO = objectMapper.readValue(response.getContentAsString(), UserEditDTO.class);

        assertEquals(userEditDTO.getLastName(), responseDTO.getLastName());
        assertEquals(userEditDTO.getFirstName(), responseDTO.getFirstName());
        assertEquals(userEditDTO.getEmail(), responseDTO.getEmail());

        Mockito.verify(userServiceMocked, Mockito.times(1)).editUser(
                Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()));
    }

    @Test
    public void editUser_invalidFirstName() throws Exception {

        UserEditDTO userEditDTO = new UserEditDTO("", null, null);

        User user = new User("username", "password",
                "firstname", "lastname", "email@gmail.com" );

        Mockito.when(userServiceMocked.editUser(Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()))).thenThrow(new FirstNameNotValid());

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("username");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/user/edit-profile")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
                .content(objectMapper.writeValueAsString(userEditDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        String message = response.getContentAsString();

        assertEquals("First name not valid", message);

        Mockito.verify(userServiceMocked, Mockito.times(1)).editUser(
                Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()));
    }

    @Test
    public void editUser_invalidLastName() throws Exception {

        UserEditDTO userEditDTO = new UserEditDTO(null, "", null);

        User user = new User("username", "password",
                "firstname", "lastname", "email@gmail.com" );

        Mockito.when(userServiceMocked.editUser(Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()))).thenThrow(new LastNameNotValid());

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("username");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/user/edit-profile")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
                .content(objectMapper.writeValueAsString(userEditDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        String message = response.getContentAsString();

        assertEquals("Last name not valid", message);

        Mockito.verify(userServiceMocked, Mockito.times(1)).editUser(
                Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()));
    }

    @Test
    public void editUser_invalidEmail() throws Exception {

        UserEditDTO userEditDTO = new UserEditDTO(null, null, "someinvalidemail");

        User user = new User("username", "password",
                "firstname", "lastname", "email@gmail.com" );

        Mockito.when(userServiceMocked.editUser(Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()))).thenThrow(new EmailNotValid());

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("username");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/user/edit-profile")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
                .content(objectMapper.writeValueAsString(userEditDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        String message = response.getContentAsString();

        assertEquals("Email not valid", message);

        Mockito.verify(userServiceMocked, Mockito.times(1)).editUser(
                Mockito.any(UserEditDTO.class), Mockito.eq(user.getUsername()));
    }

    @Test
    public void editUser_userNotFound() throws Exception {

        UserEditDTO userEditDTO = new UserEditDTO("firstnamechanged", null, null);

        Mockito.when(userServiceMocked.editUser(Mockito.any(UserEditDTO.class), Mockito.eq("someinvalidusername"))).thenThrow(new UserNotFound());

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("someinvalidusername");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/user/edit-profile")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
                .content(objectMapper.writeValueAsString(userEditDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        String message = response.getContentAsString();

        assertEquals("User not found", message);

        Mockito.verify(userServiceMocked, Mockito.times(1)).editUser(
                Mockito.any(UserEditDTO.class), Mockito.eq("someinvalidusername"));

    }
}