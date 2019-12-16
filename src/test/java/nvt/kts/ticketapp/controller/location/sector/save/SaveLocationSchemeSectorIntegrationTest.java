package nvt.kts.ticketapp.controller.location.sector.save;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.dto.location.LocationSchemeSectorsDTO;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class SaveLocationSchemeSectorIntegrationTest {

    private static final String URL_PREFIX = "/api/locationSchemeSector";

    @Autowired
    private TestRestTemplate restTemplate;

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private LocationSchemeRepository locationSchemeRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationSectorRepository locationSectorRepository;
    @Autowired
    private SectorRepository sectorRepository;

    private final Long EXISTING_SCHEME_ID = 1L;
    private final Long EXISTING_SCHEME2_ID = 2L;
    private final String SPENS_NAME = "Spens";

    private static LocationSchemeDTO spensScheme;
    private static SectorDTO westGrandstand;
    private static SectorDTO eastGrandstand;
    private static LocationSector locationSector;
    private static LocationSchemeSectorsDTO wrapper;

    @Before
    public void init() throws LocationSchemeAlreadyExists {
        spensScheme = new LocationSchemeDTO(false, SPENS_NAME, "Maksima Gorkog");
        westGrandstand = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);
        eastGrandstand = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);

        wrapper = new LocationSchemeSectorsDTO(Arrays.asList(westGrandstand, eastGrandstand),
                spensScheme);
    }


    /**
     * Test saving new location scheme with its sectors
     *
     * @throws LocationSchemeAlreadyExists
     */
    @Test
    public void save_Positive() throws LocationSchemeAlreadyExists {
        ResponseEntity<LocationSchemeSectorsDTO> response = restTemplate.withBasicAuth("admin", "password").
                postForEntity(URL_PREFIX, wrapper, LocationSchemeSectorsDTO.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test saving location scheme that already exists, fails
     *
     * @throws LocationSchemeAlreadyExists
     */
    @Test
    public void save_Negative() throws LocationSchemeAlreadyExists {
        locationSchemeRepository.save(new LocationScheme(SPENS_NAME, "Another address"));
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin", "password").
                postForEntity(URL_PREFIX, wrapper, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
