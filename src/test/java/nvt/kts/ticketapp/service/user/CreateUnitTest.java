package nvt.kts.ticketapp.service.user;


import nvt.kts.ticketapp.domain.dto.user.UserRegistrationDTO;
import nvt.kts.ticketapp.domain.model.user.Authority;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.user.*;
import nvt.kts.ticketapp.repository.user.AdminRepository;
import nvt.kts.ticketapp.repository.user.AuthorityRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateUnitTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepositoryMocked;

    @MockBean
    private AdminRepository adminRepositoryMocked;

    @MockBean
    private AuthorityRepository authorityRepositoryMocked;

    @Test
    public void create_success() throws EmailAlreadyExist, EmailNotValid, AuthorityDoesNotExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {

        UserRegistrationDTO userRegistrationDTO =
                new UserRegistrationDTO(null, "username", "Password123!", "firstname", "lastname", "email@gmail.com");

        User user = new User("username","Password123!", "firstname", "lastname", "email@gmail.com");

        Authority authority = new Authority("ROLE_REGISTERED");

        List<Authority> authorities = new ArrayList<>();
        authorities.add(authority);
        user.setAuthorities(authorities);

        Mockito.when(userRepositoryMocked.findOneByUsername(userRegistrationDTO.getUsername())).thenReturn(Optional.empty());
        Mockito.when(adminRepositoryMocked.findOneByUsername(userRegistrationDTO.getUsername())).thenReturn(Optional.empty());
        Mockito.when(userRepositoryMocked.findOneByEmail(userRegistrationDTO.getEmail())).thenReturn(Optional.empty());
        Mockito.when(authorityRepositoryMocked.findOneById(1L)).thenReturn(Optional.of(authority));
        Mockito.when(userRepositoryMocked.save(Mockito.any(User.class))).thenReturn(user);

        User createdUser = userService.create(userRegistrationDTO);

        assertNotNull(createdUser);
        assertEquals("username", createdUser.getUsername());
        assertTrue(new BCryptPasswordEncoder().matches("Password123!", createdUser.getPassword()));
        assertEquals("firstname", createdUser.getFirstName());
        assertEquals("lastname", createdUser.getLastName());
        assertEquals(1, createdUser.getAuthorities().size());

        Authority userAuthority = (Authority) createdUser.getAuthorities().toArray()[0];

        assertEquals("ROLE_REGISTERED", userAuthority.getName());

        Mockito.verify(userRepositoryMocked).findOneByUsername(userRegistrationDTO.getUsername());
        Mockito.verify(adminRepositoryMocked).findOneByUsername(userRegistrationDTO.getUsername());
        Mockito.verify(userRepositoryMocked).findOneByEmail(userRegistrationDTO.getEmail());
        Mockito.verify(authorityRepositoryMocked).findOneById(1L);
        Mockito.verify(userRepositoryMocked).save(Mockito.any(User.class));

    }

    @Test(expected = EmailAlreadyExist.class)
    public void create_EmailAlreadyExist() throws EmailAlreadyExist, EmailNotValid, AuthorityDoesNotExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {

        UserRegistrationDTO userRegistrationDTO =
                new UserRegistrationDTO(null, "username", "Password123!", "firstname", "lastname", "email@gmail.com");

        User user = new User("username","Password123!", "firstname", "lastname", "email@gmail.com");

        Mockito.when(userRepositoryMocked.findOneByUsername(userRegistrationDTO.getUsername())).thenReturn(Optional.empty());
        Mockito.when(adminRepositoryMocked.findOneByUsername(userRegistrationDTO.getUsername())).thenReturn(Optional.empty());
        Mockito.when(userRepositoryMocked.findOneByEmail(userRegistrationDTO.getEmail())).thenReturn(Optional.of(user));

        User createdUser = userService.create(userRegistrationDTO);

        Mockito.verify(userRepositoryMocked).findOneByUsername(userRegistrationDTO.getUsername());
        Mockito.verify(adminRepositoryMocked).findOneByUsername(userRegistrationDTO.getUsername());
        Mockito.verify(userRepositoryMocked).findOneByEmail(userRegistrationDTO.getEmail());
        Mockito.verify(authorityRepositoryMocked, Mockito.times(0)).findOneById(1L);
        Mockito.verify(userRepositoryMocked, Mockito.times(0)).save(Mockito.any(User.class));
    }

    @Test(expected = EmailNotValid.class)
    public void create_EmailNotValid() throws EmailAlreadyExist, EmailNotValid, AuthorityDoesNotExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {

        UserRegistrationDTO userRegistrationDTO =
                new UserRegistrationDTO(null, "username", "Password123!", "firstname", "lastname", "email@gmail");

        Mockito.when(userRepositoryMocked.findOneByUsername(userRegistrationDTO.getUsername())).thenReturn(Optional.empty());
        Mockito.when(adminRepositoryMocked.findOneByUsername(userRegistrationDTO.getUsername())).thenReturn(Optional.empty());
        Mockito.when(userRepositoryMocked.findOneByEmail(userRegistrationDTO.getEmail())).thenReturn(Optional.empty());

        User createdUser = userService.create(userRegistrationDTO);

        Mockito.verify(userRepositoryMocked).findOneByUsername(userRegistrationDTO.getUsername());
        Mockito.verify(adminRepositoryMocked).findOneByUsername(userRegistrationDTO.getUsername());
        Mockito.verify(userRepositoryMocked).findOneByEmail(userRegistrationDTO.getEmail());
        Mockito.verify(authorityRepositoryMocked, Mockito.times(0)).findOneById(1L);
        Mockito.verify(userRepositoryMocked, Mockito.times(0)).save(Mockito.any(User.class));

    }

    @Test(expected = AuthorityDoesNotExist.class)
    public void create_AuthorityDoesNotExist() throws EmailAlreadyExist, EmailNotValid, AuthorityDoesNotExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {

        UserRegistrationDTO userRegistrationDTO =
                new UserRegistrationDTO(null, "username", "Password123!", "firstname", "lastname", "email@gmail.com");

        Mockito.when(userRepositoryMocked.findOneByUsername(userRegistrationDTO.getUsername())).thenReturn(Optional.empty());
        Mockito.when(adminRepositoryMocked.findOneByUsername(userRegistrationDTO.getUsername())).thenReturn(Optional.empty());
        Mockito.when(userRepositoryMocked.findOneByEmail(userRegistrationDTO.getEmail())).thenReturn(Optional.empty());
        Mockito.when(authorityRepositoryMocked.findOneById(1L)).thenReturn(Optional.empty());

        User createdUser = userService.create(userRegistrationDTO);

        Mockito.verify(userRepositoryMocked).findOneByUsername(userRegistrationDTO.getUsername());
        Mockito.verify(adminRepositoryMocked).findOneByUsername(userRegistrationDTO.getUsername());
        Mockito.verify(userRepositoryMocked).findOneByEmail(userRegistrationDTO.getEmail());
        Mockito.verify(authorityRepositoryMocked).findOneById(1L);
        Mockito.verify(userRepositoryMocked, Mockito.times(0)).save(Mockito.any(User.class));

    }

    @Test(expected = UsernameNotValid.class)
    public void create_UsernameNotValid() throws EmailAlreadyExist, EmailNotValid, AuthorityDoesNotExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {

        UserRegistrationDTO userRegistrationDTO =
                new UserRegistrationDTO(null, "1username", "Password123!", "firstname", "lastname", "email@gmail.com");

        Mockito.when(userRepositoryMocked.findOneByUsername(userRegistrationDTO.getUsername())).thenReturn(Optional.empty());
        Mockito.when(adminRepositoryMocked.findOneByUsername(userRegistrationDTO.getUsername())).thenReturn(Optional.empty());
        Mockito.when(userRepositoryMocked.findOneByEmail(userRegistrationDTO.getEmail())).thenReturn(Optional.empty());

        User createdUser = userService.create(userRegistrationDTO);

        Mockito.verify(userRepositoryMocked).findOneByUsername(userRegistrationDTO.getUsername());
        Mockito.verify(adminRepositoryMocked).findOneByUsername(userRegistrationDTO.getUsername());
        Mockito.verify(userRepositoryMocked).findOneByEmail(userRegistrationDTO.getEmail());
        Mockito.verify(authorityRepositoryMocked, Mockito.times(0)).findOneById(1L);
        Mockito.verify(userRepositoryMocked, Mockito.times(0)).save(Mockito.any(User.class));

    }

    @Test(expected = UsernameAlreadyExist.class)
    public void create_UsernameAlreadyExist() throws EmailAlreadyExist, EmailNotValid, AuthorityDoesNotExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {

        UserRegistrationDTO userRegistrationDTO =
                new UserRegistrationDTO(null, "1username", "Password123!", "firstname", "lastname", "email@gmail.com");

        User user = new User("username","Password123!", "firstname", "lastname", "email@gmail.com");

        Mockito.when(userRepositoryMocked.findOneByUsername(userRegistrationDTO.getUsername())).thenReturn(Optional.of(user));

        User createdUser = userService.create(userRegistrationDTO);

        Mockito.verify(userRepositoryMocked).findOneByUsername(userRegistrationDTO.getUsername());
        Mockito.verify(adminRepositoryMocked, Mockito.times(0)).findOneByUsername(userRegistrationDTO.getUsername());
        Mockito.verify(userRepositoryMocked, Mockito.times(0)).findOneByEmail(userRegistrationDTO.getEmail());
        Mockito.verify(authorityRepositoryMocked, Mockito.times(0)).findOneById(1L);
        Mockito.verify(userRepositoryMocked, Mockito.times(0)).save(Mockito.any(User.class));

    }

    @Test(expected = PasswordNotValid.class)
    public void create_PasswordNotValid() throws EmailAlreadyExist, EmailNotValid, AuthorityDoesNotExist, UsernameNotValid, UsernameAlreadyExist, PasswordNotValid {

        UserRegistrationDTO userRegistrationDTO =
                new UserRegistrationDTO(null, "username", "password1", "firstname", "lastname", "email@gmail.com");

        Mockito.when(userRepositoryMocked.findOneByUsername(userRegistrationDTO.getUsername())).thenReturn(Optional.empty());
        Mockito.when(adminRepositoryMocked.findOneByUsername(userRegistrationDTO.getUsername())).thenReturn(Optional.empty());
        Mockito.when(userRepositoryMocked.findOneByEmail(userRegistrationDTO.getEmail())).thenReturn(Optional.empty());

        User createdUser = userService.create(userRegistrationDTO);

        Mockito.verify(userRepositoryMocked).findOneByUsername(userRegistrationDTO.getUsername());
        Mockito.verify(adminRepositoryMocked).findOneByUsername(userRegistrationDTO.getUsername());
        Mockito.verify(userRepositoryMocked).findOneByEmail(userRegistrationDTO.getEmail());
        Mockito.verify(authorityRepositoryMocked, Mockito.times(0)).findOneById(1L);
        Mockito.verify(userRepositoryMocked, Mockito.times(0)).save(Mockito.any(User.class));

    }

}
