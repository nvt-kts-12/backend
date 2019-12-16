package nvt.kts.ticketapp.controller.location.sector.delete;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.dto.location.LocationSchemeSectorsDTO;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeCanNotBeDeleted;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.exception.sector.CanNotDeleteSchemeSectors;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class DeleteLocationSchemeSectorControllerIntegrationTest {


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
    private static Location spens;
    private static Sector westGrandstand;
    private static Sector eastGrandstand;
    private static LocationSector locationSector;

    private static LocationSchemeSectorsDTO wrapper;

    @Before
    public void init() throws LocationSchemeAlreadyExists {
        spensScheme = locationSchemeRepository.save(new LocationScheme(SPENS_NAME, "Maksima Gorkog"));
        westGrandstand = sectorRepository.save(new Sector(10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, spensScheme));
        eastGrandstand = sectorRepository.save(new Sector(10.0, 10.0,
                0.0, 0.0, 150,
                15, 10, SectorType.GRANDSTAND, spensScheme));

        wrapper = new LocationSchemeSectorsDTO(Arrays.asList(ObjectMapperUtils.map(westGrandstand, SectorDTO.class),
                ObjectMapperUtils.map(eastGrandstand, SectorDTO.class)),
                ObjectMapperUtils.map(spensScheme, LocationSchemeDTO.class));
    }


    /**
     * Test deleting Location Scheme and its sectors
     *
     * @throws CanNotDeleteSchemeSectors
     * @throws LocationSchemeDoesNotExist
     * @throws LocationSchemeCanNotBeDeleted
     */
    @Test
    public void delete_Positive() throws CanNotDeleteSchemeSectors, LocationSchemeDoesNotExist, LocationSchemeCanNotBeDeleted {
        ResponseEntity<LocationSchemeSectorsDTO> response =
                restTemplate.withBasicAuth("admin", "password").exchange(URL_PREFIX, HttpMethod.DELETE,
                        new HttpEntity<LocationSchemeSectorsDTO>(wrapper), LocationSchemeSectorsDTO.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getLocationScheme().isDeleted());
        assertNotNull(response.getBody().getSectors());
        assertEquals(2, response.getBody().getSectors().size());
        assertTrue(response.getBody().getSectors().get(0).isDeleted());
    }

    /**
     * Test deleting location scheme with NONEXISTENT_SCHEME_ID == 2L, fails
     *
     * @throws CanNotDeleteSchemeSectors
     * @throws LocationSchemeDoesNotExist
     * @throws LocationSchemeCanNotBeDeleted
     */
    @Test
    public void delete_Negative_LocationSchemeDoesNotExist() throws CanNotDeleteSchemeSectors, LocationSchemeDoesNotExist, LocationSchemeCanNotBeDeleted {
        wrapper.setLocationScheme(new LocationSchemeDTO(false,"NONEXISTENT", "NONEXISTENT"));
        ResponseEntity<String> response =
                restTemplate.withBasicAuth("admin", "password").exchange(URL_PREFIX, HttpMethod.DELETE,
                        new HttpEntity<LocationSchemeSectorsDTO>(wrapper), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test deleting location scheme sectors that are booked on some event
     *
     * @throws CanNotDeleteSchemeSectors
     * @throws LocationSchemeDoesNotExist
     * @throws LocationSchemeCanNotBeDeleted
     */
    @Test
    public void delete_Negative_CanNotDeleteSchemeSectors() throws CanNotDeleteSchemeSectors, LocationSchemeDoesNotExist, LocationSchemeCanNotBeDeleted {
        spens = locationRepository.save(new Location(spensScheme));
        locationSectorRepository.save(new LocationSector(westGrandstand, spens,
                150.0, 100, true));

        ResponseEntity<String> response =
                restTemplate.withBasicAuth("admin", "password").exchange(URL_PREFIX, HttpMethod.DELETE,
                        new HttpEntity<LocationSchemeSectorsDTO>(wrapper), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test deleting location scheme that is booked on some event
     * @throws CanNotDeleteSchemeSectors
     * @throws LocationSchemeDoesNotExist
     * @throws LocationSchemeCanNotBeDeleted
     */
    @Test
    public void delete_Negative_LocationSchemeCanNotBeDeleted() throws CanNotDeleteSchemeSectors, LocationSchemeDoesNotExist, LocationSchemeCanNotBeDeleted {
        spens = locationRepository.save(new Location(spensScheme));

        ResponseEntity<String> response =
                restTemplate.withBasicAuth("admin", "password").exchange(URL_PREFIX, HttpMethod.DELETE,
                        new HttpEntity<LocationSchemeSectorsDTO>(wrapper), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
