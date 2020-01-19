package nvt.kts.ticketapp.service.user.show;


import nvt.kts.ticketapp.domain.dto.user.UserDTO;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.UserNotFound;
import nvt.kts.ticketapp.repository.user.UserRepository;
import nvt.kts.ticketapp.service.user.UserService;
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
public class ShowUserUnitTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepositoryMocked;

    @Test
    public void showUser_success() throws UserNotFound {

        String username = "testusername";

        User user = new User("testusername", "testpassword",
                "testfirstname", "testlastname", "testmail@gmail.com" );

        Mockito.when(userRepositoryMocked.findOneByUsername(username)).thenReturn(Optional.of(user));

        User foundUser = (User) userService.findByUsername(username);

        assertNotNull(foundUser);
        assertEquals(foundUser.getUsername(), user.getUsername());
        assertEquals(foundUser.getFirstName(), user.getFirstName());
        assertEquals(foundUser.getLastName(), user.getLastName());
        assertEquals(foundUser.getEmail(), user.getEmail());
        assertEquals(foundUser.getPassword(), user.getPassword());
    }

    @Test(expected = UserNotFound.class)
    public void showUser_userNotFound() throws UserNotFound {

        String username = "testusername";

        userService.findByUsername(username);

    }

}
