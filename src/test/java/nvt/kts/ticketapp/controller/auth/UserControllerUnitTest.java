package nvt.kts.ticketapp.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nvt.kts.ticketapp.domain.dto.user.UserEditDTO;
import nvt.kts.ticketapp.domain.model.user.User;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerUnitTest {

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

        String dtoAsJsonString = getUserEditInJson(userEditDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/user/edit-profile")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
                .content(dtoAsJsonString))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        UserEditDTO responseDTO = objectMapper.readValue(response.getContentAsString(), UserEditDTO.class);

        assertEquals(userEditDTO.getFirstName(), responseDTO.getFirstName());
        assertEquals(user.getLastName(), responseDTO.getLastName());
        assertEquals(user.getEmail(), responseDTO.getEmail());
    }

    private String getUserEditInJson(UserEditDTO userEditDTO) {
        return "{ \"firstName\" : " + (userEditDTO.getFirstName() != null ? ("\"" + userEditDTO.getFirstName() + "\"") : "null") +
                ", \"lastName\" : " + (userEditDTO.getLastName() != null ? ("\"" + userEditDTO.getLastName() + "\"") : "null") +
                ", \"email\": " + (userEditDTO.getEmail() != null ? ("\"" + userEditDTO.getEmail() + "\"") : "null") + "}";
    }
}