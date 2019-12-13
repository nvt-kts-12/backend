package nvt.kts.ticketapp.controller.location;

import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.SectorType;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.service.location.LocationSchemeServiceImpl;
import nvt.kts.ticketapp.service.sector.SectorServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class GetLocationSchemeSectorControllerUnitTest {


    @LocalServerPort
    int randomServerPort;

    private static final String URL_PREFIX = "/api/locationSchemeSector";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private LocationSchemeServiceImpl locationSchemeService;

    @MockBean
    private SectorServiceImpl sectorService;

    private final Long EXISTING_SCHEME_ID = 1L;


    @Before
    public void init() throws LocationSchemeAlreadyExists {

        SectorDTO sector = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);
        SectorDTO sector2 = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);
        SectorDTO sector3 = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                0, 0, SectorType.PARTER);
        List<SectorDTO> sectors = Arrays.asList(sector, sector2, sector3);

        when(sectorService.getByScheme(EXISTING_SCHEME_ID)).thenReturn(sectors);
    }

    /**
     * Test getting sectors by location scheme with id EXISTING_SCHEME_ID == 1L
     */
    @Test
    public void getByScheme_Positive() {
        ResponseEntity<List<SectorDTO>> response = testRestTemplate.withBasicAuth("username", "password").exchange(URL_PREFIX + "/1",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<SectorDTO>>() {
                });

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());

        verify(sectorService, times(1)).getByScheme(EXISTING_SCHEME_ID);
    }
}
