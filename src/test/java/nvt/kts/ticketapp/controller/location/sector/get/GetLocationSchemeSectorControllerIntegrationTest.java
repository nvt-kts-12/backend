package nvt.kts.ticketapp.controller.location.sector.get;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.domain.model.location.SectorType;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class GetLocationSchemeSectorControllerIntegrationTest {


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

    private static LocationScheme spensScheme;
    private static Sector westGrandstand;
    private static Sector eastGrandstand;
    private static LocationSector locationSector;

    @Before
    public void init() throws LocationSchemeAlreadyExists {
        spensScheme = locationSchemeRepository.save(new LocationScheme(SPENS_NAME, "Maksima Gorkog"));
        westGrandstand = sectorRepository.save(new Sector(10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, spensScheme));
        eastGrandstand = sectorRepository.save(new Sector(10.0, 10.0,
                0.0, 0.0, 150,
                15, 10, SectorType.GRANDSTAND, spensScheme));

    }

    /**
     * Test getting sectors by location scheme with id EXISTING_SCHEME_ID == 1L
     */
    @Test
    public void getByScheme_Positive() {
        ResponseEntity<List<SectorDTO>> response = restTemplate.
                withBasicAuth("username", "password").exchange(URL_PREFIX + "/" + spensScheme.getId(),
                HttpMethod.GET, null, new ParameterizedTypeReference<List<SectorDTO>>() {
                });

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2 , response.getBody().size());
    }
}
