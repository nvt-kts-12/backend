package nvt.kts.ticketapp.repository.locationScheme;

import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.html.Option;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LocationSchemeRepositoryTest {

    @Autowired
    private LocationSchemeRepository locationSchemeRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private final String EXISTING_SCHEME_NAME = "Scheme 1";
    LocationScheme locationScheme;

    @Before
    public void setUp() throws Exception {
        locationScheme = new LocationScheme(EXISTING_SCHEME_NAME, "Address 1");
        locationScheme.setDeleted(false);

        LocationScheme locationScheme2 = new LocationScheme("Scheme 2", "Address 2");
        locationScheme2.setDeleted(false);

        LocationScheme locationScheme3 = new LocationScheme("Scheme 3", "Address 3");
        locationScheme2.setDeleted(true);


        testEntityManager.persistAndFlush(locationScheme);
        testEntityManager.persistAndFlush(locationScheme2);
        testEntityManager.persistAndFlush(locationScheme3);
    }

    @Test
    public void findByNameIgnoreCaseAndDeletedFalse_Positive() {
        Optional<LocationScheme> foundScheme = locationSchemeRepository.
                findByNameIgnoreCaseAndDeletedFalse(EXISTING_SCHEME_NAME);

        assertNotNull(foundScheme.get());
        assertEquals(EXISTING_SCHEME_NAME, foundScheme.get().getName());
        assertFalse(foundScheme.get().isDeleted());
    }

    @Test
    public void findByIdAndDeletedFalse_Positive() {
        Optional<LocationScheme> foundScheme = locationSchemeRepository.
                findByIdAndDeletedFalse(locationScheme.getId());

        assertNotNull(foundScheme);
        assertEquals(locationScheme.getId(), foundScheme.get().getId());
        assertFalse(foundScheme.get().isDeleted());
    }

    @Test
    public void findAllByDeletedFalse_Positive() {
        List<LocationScheme> foundSchemes = locationSchemeRepository.
                findAllByDeletedFalse();

        assertNotNull(foundSchemes);
        assertEquals(2, foundSchemes.size());
        assertFalse(foundSchemes.get(0).isDeleted());
        assertFalse(foundSchemes.get(1).isDeleted());
    }
}