package nvt.kts.ticketapp.controller.location.sector;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.domain.model.location.SectorType;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class SectorControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String URL_PREFIX = "/api/sector";
    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private LocationSectorRepository locationSectorRepository;

    @Autowired
    private LocationSchemeRepository locationSchemeRepository;

    private static Sector sector_01;
    private static Sector sector_02;

    private static LocationScheme spensScheme;
    @Before
    public void setUp(){
        spensScheme = locationSchemeRepository.save(new LocationScheme("Spens", "Maksima Gorkog"));
        sector_01 = sectorRepository.save(new Sector(10.0, 10.0, 0.0, 0.0,
                100, 10, 10, SectorType.GRANDSTAND, spensScheme));

        sector_02 = sectorRepository.save(new Sector(10.0, 10.0, 0.0, 0.0,
                150, 15, 10, SectorType.GRANDSTAND, spensScheme));
    }


    /**
     * Test getting all sectors
     */
    @Test
    public void getAll() {
        ResponseEntity<List<SectorDTO>> response = restTemplate.withBasicAuth("username", "password")
                .exchange(URL_PREFIX,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<SectorDTO>>() {
                        });

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    /**
     * Test getting Sector with EXISTING_SERVICE_ID == 1L
     */
    @Test
    public void get() throws SectorDoesNotExist {
        ResponseEntity<SectorDTO> response = restTemplate.withBasicAuth("username", "password")
                .getForEntity(URL_PREFIX + "/" + sector_01.getId(), SectorDTO.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sector_01.getId(), response.getBody().getId());
    }

    /**
     * Test getting Sector with NONEXISTENT_SERVICE_ID , fails because there is no such sector
     */
    @Test
    public void get_Negative_SectorDoesNotExist() throws SectorDoesNotExist {
        ResponseEntity<String> response = restTemplate.withBasicAuth("username", "password")
                .getForEntity(URL_PREFIX + "/5", String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
