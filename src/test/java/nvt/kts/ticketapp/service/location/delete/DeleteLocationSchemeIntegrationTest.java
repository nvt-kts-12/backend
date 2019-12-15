package nvt.kts.ticketapp.service.location.delete;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeCanNotBeDeleted;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
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

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteLocationSchemeIntegrationTest {

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
    private static Location spens;

    private final String MARAKANA_NAME = "Stadion Rajko Mitic";
    private final String SPENS_NAME = "Spens";
    private final Long NONEXISTENT_SCHEME_ID = 5L;

    @Before
    public void setUp() {
        marakanaScheme = new LocationScheme(MARAKANA_NAME, "Ljutice Bogdana");
        spensScheme = new LocationScheme(SPENS_NAME, "Maksima Gorkog");

        LocationScheme savedMarakana = locationSchemeRepository.save(marakanaScheme);
        LocationScheme savedSpens = locationSchemeRepository.save(spensScheme);

        spens = new Location(savedSpens);
        locationRepository.save(spens);
    }

    /**
     * Test deleting existing location scheme
     *
     * @throws LocationSchemeDoesNotExist
     * @throws LocationSchemeCanNotBeDeleted
     */
    @Test
    public void deleteScheme_Positive() throws LocationSchemeDoesNotExist, LocationSchemeCanNotBeDeleted {
        LocationSchemeDTO deletedMarakana = locationSchemeService.delete(marakanaScheme.getId());

        assertNotNull(deletedMarakana);
        assertEquals(marakanaScheme.getId(), deletedMarakana.getId());
        assertEquals(MARAKANA_NAME, deletedMarakana.getName());
        assertTrue(deletedMarakana.isDeleted());
    }

    /**
     * Test deleting scheme that already has location created and assigned to event, fails
     *
     * @throws LocationSchemeDoesNotExist
     * @throws LocationSchemeCanNotBeDeleted
     */
    @Test(expected = LocationSchemeCanNotBeDeleted.class)
    public void deleteScheme_Negative_CanNotBeDeleted() throws LocationSchemeDoesNotExist, LocationSchemeCanNotBeDeleted {
        LocationSchemeDTO deletedSpens = locationSchemeService.delete(spensScheme.getId());
    }

    /**
     * Test deleting scheme with NONEXISTENT_SCHEME_ID == 5L, fails because such scheme does not exist
     * @throws LocationSchemeDoesNotExist
     * @throws LocationSchemeCanNotBeDeleted
     */
    @Test(expected = LocationSchemeDoesNotExist.class)
    public void deleteScheme_Negative_DoesNotExist() throws LocationSchemeDoesNotExist, LocationSchemeCanNotBeDeleted {
        LocationSchemeDTO deletedScheme = locationSchemeService.delete(NONEXISTENT_SCHEME_ID);
    }


}
