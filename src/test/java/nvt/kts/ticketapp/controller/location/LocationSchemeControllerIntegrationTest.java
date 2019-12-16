package nvt.kts.ticketapp.controller.location;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class LocationSchemeControllerIntegrationTest {

    private static final String URL_PREFIX = "/api/locationScheme";

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private TestRestTemplate restTemplate;

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
     * Test getting existing LocationScheme
     *
     * @throws LocationSchemeDoesNotExist
     */
    @Test
    public void get_Positive() throws LocationSchemeDoesNotExist {
        ResponseEntity<LocationSchemeDTO> providedMarakana = restTemplate.withBasicAuth("username", "password")
                .getForEntity(URL_PREFIX + "/" + marakanaScheme.getId(), LocationSchemeDTO.class);

        assertNotNull(providedMarakana.getBody());
        assertEquals(HttpStatus.OK, providedMarakana.getStatusCode());
        assertEquals(marakanaScheme.getId(), providedMarakana.getBody().getId());
        assertEquals(MARAKANA_NAME, providedMarakana.getBody().getName());
    }

    /**
     * Test getting LocationScheme with id NONEXISTENT_SCHEME_ID == 5L fails
     *
     * @throws LocationSchemeDoesNotExist
     */
    @Test
    public void get_Negative() throws LocationSchemeDoesNotExist {
        ResponseEntity<String> response = restTemplate.withBasicAuth("username", "password")
                .getForEntity(URL_PREFIX + "/19", String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test getting all location schemas
     */
    @Test
    public void getAll_Positive() {
        ResponseEntity<List<LocationSchemeDTO>> allSchemes = restTemplate.withBasicAuth("username", "password")
                .exchange(URL_PREFIX, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<LocationSchemeDTO>>() {
                        });

        assertNotNull(allSchemes.getBody());
        assertEquals(HttpStatus.OK, allSchemes.getStatusCode());
        assertEquals(2, allSchemes.getBody().size());
    }


}
