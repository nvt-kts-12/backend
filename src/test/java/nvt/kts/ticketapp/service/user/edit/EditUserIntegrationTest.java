package nvt.kts.ticketapp.service.user.edit;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.user.UserEditDTO;
import nvt.kts.ticketapp.domain.model.user.Authority;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.EmailNotValid;
import nvt.kts.ticketapp.exception.user.FirstNameNotValid;
import nvt.kts.ticketapp.exception.user.LastNameNotValid;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EditUserIntegrationTest {

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
    public void editUserFirstName_success() throws FirstNameNotValid, LastNameNotValid, UserNotFound, EmailNotValid {

        UserEditDTO userEditDTO = new UserEditDTO("testfirstnamechanged", null, null);

        User editedUser = userService.editUser(userEditDTO, "username");

        assertNotNull(editedUser);
        assertEquals(userEditDTO.getFirstName(), editedUser.getFirstName());
    }

    @Test
    public void editUserLastName_success() throws FirstNameNotValid, LastNameNotValid, EmailNotValid, UserNotFound {

        UserEditDTO userEditDTO = new UserEditDTO(null, "testlastnamechanged", null);

        User editedUser = userService.editUser(userEditDTO, "username");

        assertNotNull(editedUser);
        assertEquals(userEditDTO.getLastName(), editedUser.getLastName());
    }

    @Test
    public void editUserEmail_success() throws FirstNameNotValid, LastNameNotValid, UserNotFound, EmailNotValid {

        UserEditDTO userEditDTO = new UserEditDTO(null, null, "testmailchanged@gmail.com");

        User editedUser = userService.editUser(userEditDTO, "username");

        assertEquals(userEditDTO.getEmail(), editedUser.getEmail());
    }

    @Test
    public void editUser_success() throws FirstNameNotValid, LastNameNotValid, UserNotFound, EmailNotValid {

        UserEditDTO userEditDTO = new UserEditDTO("testfirstnamechanged",
                "testlastnamechanged", "testmailchanged@gmail.com");

        User editedUser = userService.editUser(userEditDTO, "username");

        assertEquals(userEditDTO.getFirstName(), editedUser.getFirstName());
        assertEquals(userEditDTO.getLastName(), editedUser.getLastName());
        assertEquals(userEditDTO.getEmail(), editedUser.getEmail());
    }

    @Test(expected = FirstNameNotValid.class)
    public void editUser_invalidFirstName() throws FirstNameNotValid, LastNameNotValid, UserNotFound, EmailNotValid {

        UserEditDTO userEditDTO = new UserEditDTO("",null, null);
        userService.editUser(userEditDTO, "username");
    }

    @Test(expected = LastNameNotValid.class)
    public void editUser_invalidLastName() throws FirstNameNotValid, LastNameNotValid, UserNotFound, EmailNotValid {

        UserEditDTO userEditDTO = new UserEditDTO(null,"", null);
        userService.editUser(userEditDTO, "username");
    }

    @Test(expected = EmailNotValid.class)
    public void editUser_invalidEmail() throws FirstNameNotValid, LastNameNotValid, UserNotFound, EmailNotValid {

        UserEditDTO userEditDTO = new UserEditDTO(null,null, "someinvalidmail");
        userService.editUser(userEditDTO, "username");
    }

    @Test(expected = UserNotFound.class)
    public void editUser_userNotFound() throws FirstNameNotValid, LastNameNotValid, UserNotFound, EmailNotValid {
        UserEditDTO userEditDTO = new UserEditDTO("testfirstnamechanged",null, null);
        userService.editUser(userEditDTO, "someinvalidusername");
    }

}
