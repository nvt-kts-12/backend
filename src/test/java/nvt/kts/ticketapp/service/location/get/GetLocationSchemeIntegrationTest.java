package nvt.kts.ticketapp.service.location.get;


import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetLocationSchemeIntegrationTest {

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
    private final Long NONEXISTENT_SCHEME_ID = 5L;

    @Before
    public void setUp() {
        marakanaScheme = new LocationScheme(MARAKANA_NAME, "Ljutice Bogdana");
        spensScheme = new LocationScheme(SPENS_NAME, "Maksima Gorkog");

        locationSchemeRepository.save(marakanaScheme);
        locationSchemeRepository.save(spensScheme);
    }


    /**
     * Test getting location scheme dto with id EXISTING_SCHEME_ID == 1L
     *
     * @throws LocationSchemeDoesNotExist
     */
    @Test
    public void getSchemeDto_Positive() throws LocationSchemeDoesNotExist {
        LocationSchemeDTO providedMarakanaScheme = locationSchemeService.get(marakanaScheme.getId());

        assertNotNull(providedMarakanaScheme);
        assertEquals(marakanaScheme.getId(), providedMarakanaScheme.getId());
        assertEquals(MARAKANA_NAME, providedMarakanaScheme.getName());
    }

    /**
     * Test getting location scheme dto with id NONEXISTENT_SCHEME_ID == 2L fails
     *
     * @throws LocationSchemeDoesNotExist
     */
    @Test(expected = LocationSchemeDoesNotExist.class)
    public void getSchemeDto_Negative() throws LocationSchemeDoesNotExist {
        LocationSchemeDTO providedMarakanaScheme = locationSchemeService.get(NONEXISTENT_SCHEME_ID);
    }

    /**
     * Test getting location scheme with EXISTING_SCHEME_ID == 1L
     *
     * @throws LocationSchemeDoesNotExist
     */
    @Test
    public void getScheme_Positive() throws LocationSchemeDoesNotExist {
        LocationScheme providedSpensScheme = locationSchemeService.getScheme(spensScheme.getId());

        assertNotNull(providedSpensScheme);
        assertEquals(spensScheme.getId(), providedSpensScheme.getId());
        assertEquals(SPENS_NAME, providedSpensScheme.getName());
    }

    /**
     * Test getting location scheme with NONEXISTENT_SCHEME_ID == 2L, fails
     *
     * @throws LocationSchemeDoesNotExist
     */
    @Test(expected = LocationSchemeDoesNotExist.class)
    public void getScheme_Negative_LocationSchemeDoesNotExist() throws LocationSchemeDoesNotExist {
        LocationScheme providedSpensScheme = locationSchemeService.getScheme(NONEXISTENT_SCHEME_ID);
    }

    /**
     * Test getting all location schemas
     */
    @Test
    public void getAll_Positive() {
        List<LocationSchemeDTO> locationSchemeDTOS = locationSchemeService.getAll();

        assertNotNull(locationSchemeDTOS);
        assertEquals(2, locationSchemeDTOS.size());
        assertEquals(marakanaScheme.getId(), locationSchemeDTOS.get(0).getId());
        assertEquals(spensScheme.getId(), locationSchemeDTOS.get(1).getId());
    }


}
