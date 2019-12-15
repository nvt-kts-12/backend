package nvt.kts.ticketapp.service.location.save;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SaveLocationSchemeIntegrationTest {


    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private LocationSchemeService locationSchemeService;

    @Autowired
    private LocationSchemeRepository locationSchemeRepository;

    @Autowired
    private LocationRepository locationRepository;

    private static LocationScheme marakanaScheme;
    private static LocationScheme spensScheme;

    private final String MARAKANA_NAME = "Stadion Rajko Mitic";
    private final String SPENS_NAME = "Spens";

    @Before
    public void setUp() {
        marakanaScheme = new LocationScheme(MARAKANA_NAME, "Ljutice Bogdana");
        spensScheme = new LocationScheme(SPENS_NAME, "Maksima Gorkog");
    }

    /**
     * Test saving a scheme without id and with unique name, that means saving a new location scheme
     *
     * @throws LocationSchemeAlreadyExists
     */
    @Test
    public void saveNewScheme_Positive() throws LocationSchemeAlreadyExists {
        LocationScheme savedMarakana = locationSchemeRepository.save(marakanaScheme);

        assertNotNull(savedMarakana);
        assertNotNull(savedMarakana.getId());
        assertEquals(MARAKANA_NAME, savedMarakana.getName());
    }

    /**
     * Test saving new location scheme without id and with existing name,
     * fails because there can not be two schemas with same name
     *
     * @throws LocationSchemeAlreadyExists
     */
    @Test(expected = LocationSchemeAlreadyExists.class)
    public void saveNewScheme_Negative_LocationSchemeAlreadyExists() throws LocationSchemeAlreadyExists {
        LocationScheme savedMarakana = locationSchemeRepository.save(marakanaScheme);
        LocationScheme fakeMarakana = new LocationScheme(savedMarakana.getName(), "Some fake address");

        locationSchemeService.save(fakeMarakana);
    }

    /**
     * Test saving scheme with existing id, that means updating
     */
    @Test
    public void updateScheme_Positive() {
        spensScheme.setName("Stonotenisersko Prvenstvo Evrope Novi Sad");
        LocationScheme savedSpens = locationSchemeRepository.save(spensScheme);

        assertNotNull(savedSpens);
        assertEquals(spensScheme.getId(), savedSpens.getId());
        assertNotEquals(SPENS_NAME, savedSpens.getName());
    }
}
