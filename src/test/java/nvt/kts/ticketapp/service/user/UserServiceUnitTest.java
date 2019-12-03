package nvt.kts.ticketapp.service.user;

import nvt.kts.ticketapp.domain.dto.user.UserEditDTO;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.EmailNotValid;
import nvt.kts.ticketapp.exception.user.FirstNameNotValid;
import nvt.kts.ticketapp.exception.user.LastNameNotValid;
import nvt.kts.ticketapp.exception.user.UserNotFound;
import nvt.kts.ticketapp.repository.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceUnitTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepositoryMocked;

    @Before
    public void setUp()
    {
        User user = new User("testusername", "testpassword",
                "testfirstname", "testlastname", "testmail@gmail.com" );

        Mockito.when(userRepositoryMocked.findOneByUsername(user.getUsername())).thenReturn(Optional.of(user));
    }

    @Test
    public void editUserFirstName() throws FirstNameNotValid, LastNameNotValid, EmailNotValid, UserNotFound {

        UserEditDTO userEditDTO = new UserEditDTO("testfirstnamechanged", null, null);

        User editedUser = userService.editUser(userEditDTO, "testusername");

        assertEquals(userEditDTO.getFirstName(), editedUser.getFirstName());
    }

    @Test
    public void editUserLastName() throws FirstNameNotValid, LastNameNotValid, EmailNotValid, UserNotFound {

        UserEditDTO userEditDTO = new UserEditDTO(null, "testlastnamechanged", null);

        User editedUser = userService.editUser(userEditDTO, "testusername");

        assertEquals(userEditDTO.getLastName(), editedUser.getLastName());
    }

    @Test
    public void editUserEmail() throws FirstNameNotValid, LastNameNotValid, UserNotFound, EmailNotValid {

        UserEditDTO userEditDTO = new UserEditDTO(null, null, "testmailchanged@gmail.com");

        User editedUser = userService.editUser(userEditDTO, "testusername");

        assertEquals(userEditDTO.getEmail(), editedUser.getEmail());
    }

    @Test
    public void editUser() throws FirstNameNotValid, LastNameNotValid, UserNotFound, EmailNotValid {

        UserEditDTO userEditDTO = new UserEditDTO("testfirstnamechanged",
                "testlastnamechanged", "testmailchanged@gmail.com");

        User editedUser = userService.editUser(userEditDTO, "testusername");

        assertEquals(userEditDTO.getFirstName(), editedUser.getFirstName());
        assertEquals(userEditDTO.getLastName(), editedUser.getLastName());
        assertEquals(userEditDTO.getEmail(), editedUser.getEmail());
    }

    @Test(expected = FirstNameNotValid.class)
    public void editUser_invalidFirstName() throws FirstNameNotValid, LastNameNotValid, UserNotFound, EmailNotValid {

        UserEditDTO userEditDTO = new UserEditDTO("",null, null);
        userService.editUser(userEditDTO, "testusername");
    }

    @Test(expected = LastNameNotValid.class)
    public void editUser_invalidLastName() throws FirstNameNotValid, LastNameNotValid, UserNotFound, EmailNotValid {

        UserEditDTO userEditDTO = new UserEditDTO(null,"", null);
        userService.editUser(userEditDTO, "testusername");
    }

    @Test(expected = EmailNotValid.class)
    public void editUser_invalidEmail() throws FirstNameNotValid, LastNameNotValid, UserNotFound, EmailNotValid {

        UserEditDTO userEditDTO = new UserEditDTO(null,null, "someinvalidmail");
        userService.editUser(userEditDTO, "testusername");
    }

    @Test(expected = UserNotFound.class)
    public void editUser_userNotFound() throws FirstNameNotValid, LastNameNotValid, UserNotFound, EmailNotValid {
        UserEditDTO userEditDTO = new UserEditDTO("testfirstnamechanged",null, null);
        userService.editUser(userEditDTO, "someinvalidusername");
    }
}