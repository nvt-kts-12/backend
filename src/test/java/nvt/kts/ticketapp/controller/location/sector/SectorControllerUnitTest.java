package nvt.kts.ticketapp.controller.location.sector;

import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.SectorType;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.service.location.LocationSchemeServiceImpl;
import nvt.kts.ticketapp.service.location.LocationServiceImpl;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class SectorControllerUnitTest {

    @LocalServerPort
    int randomServerPort;

    private static final String URL_PREFIX = "/api/sector";

    @MockBean
    private LocationServiceImpl locationService;

    @MockBean
    private SectorServiceImpl sectorService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final Long EXISTING_SERVICE_ID = 1L;
    private final Long NONEXISTENT_SERVICE_ID = 2L;

    @Before
    public void init() throws SectorDoesNotExist {
        SectorDTO sectorDTO = new SectorDTO(false, 10.0, 10.0, 0.0, 0.0,
                100, 10, 10, SectorType.GRANDSTAND);
        sectorDTO.setId(EXISTING_SERVICE_ID);
        SectorDTO sector2DTO = new SectorDTO(false, 10.0, 10.0, 0.0, 0.0,
                100, 10, 10, SectorType.GRANDSTAND);

        when(sectorService.getAll()).thenReturn(Arrays.asList(sectorDTO, sector2DTO));
        when(sectorService.get(EXISTING_SERVICE_ID)).thenReturn(sectorDTO);
        when(sectorService.get(NONEXISTENT_SERVICE_ID)).thenThrow(SectorDoesNotExist.class);
    }

    /**
     * Test getting all sectors
     */
    @Test
    public void getAll() {
        ResponseEntity<List<SectorDTO>> response = testRestTemplate.withBasicAuth("username", "password")
                .exchange(URL_PREFIX,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<SectorDTO>>() {
                });

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(SectorType.GRANDSTAND, response.getBody().get(0).getType());

        verify(sectorService, times(1)).getAll();
    }

    /**
     * Test getting Sector with EXISTING_SERVICE_ID == 1L
     */
    @Test
    public void get() throws SectorDoesNotExist {
        ResponseEntity<SectorDTO> response = testRestTemplate.withBasicAuth("username", "password")
                .getForEntity(URL_PREFIX + "/1", SectorDTO.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EXISTING_SERVICE_ID, response.getBody().getId());

        verify(sectorService, times(1)).get(EXISTING_SERVICE_ID);
    }

    /**
     * Test getting Sector with NONEXISTENT_SERVICE_ID == 2L, fails because there is no such sector
     */
    @Test
    public void get_Negative_SectorDoesNotExist() throws SectorDoesNotExist {
        ResponseEntity<SectorDTO> response = testRestTemplate.withBasicAuth("username", "password")
                .getForEntity(URL_PREFIX + "/2", SectorDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verify(sectorService, times(1)).get(NONEXISTENT_SERVICE_ID);
    }
}