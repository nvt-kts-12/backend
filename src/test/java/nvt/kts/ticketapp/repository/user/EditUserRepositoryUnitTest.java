package nvt.kts.ticketapp.repository.user;

import nvt.kts.ticketapp.domain.model.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EditUserRepositoryUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findOneByUsername() {
        entityManager.persist(new User("username", "password",
                "firstname", "lastname", "email@gmail.com" ));

        Optional<User> user = userRepository.findOneByUsername("username");


        assertEquals(user.get().getUsername(), "username");
    }
}