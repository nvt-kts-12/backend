package nvt.kts.ticketapp.controller.location.sector.save;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.dto.location.LocationSchemeSectorsDTO;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class SaveLocationSchemeSectorControllerUnitTest {

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
    private final Long EXISTING_SCHEME2_ID = 2L;
    private final String EXISTING_SCHEME_NAME = "Existing Scheme";

    @Before
    public void init() throws LocationSchemeAlreadyExists {
        LocationScheme locationScheme = new LocationScheme("Scheme", "Address");
        locationScheme.setId(EXISTING_SCHEME_ID);

        when(locationSchemeService.save(any(LocationScheme.class))).thenReturn(locationScheme);
    }

    /**
     * Test saving new location scheme with its sectors
     * @throws LocationSchemeAlreadyExists
     */
    @Test
    public void save_Positive() throws LocationSchemeAlreadyExists {
        LocationSchemeDTO locationScheme = new LocationSchemeDTO(false, "Scheme", "Address");
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

        LocationSchemeSectorsDTO locationSchemeSectorsDTO = new LocationSchemeSectorsDTO(sectors, locationScheme);
        testRestTemplate.withBasicAuth("admin", "password").postForEntity(URL_PREFIX, locationSchemeSectorsDTO, LocationSchemeSectorsDTO.class);

        verify(sectorService, times(1)).saveAll(anyList(), any(LocationScheme.class));
        verify(locationSchemeService, times(1)).save(any(LocationScheme.class));
    }

    /**
     * Test saving location scheme that already exists, fails
     * @throws LocationSchemeAlreadyExists
     */
    @Test
    public void save_Negative() throws LocationSchemeAlreadyExists {
        LocationSchemeDTO locationScheme = new LocationSchemeDTO(false, EXISTING_SCHEME_NAME, "Address 2");
        locationScheme.setId(EXISTING_SCHEME2_ID);
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

        LocationSchemeSectorsDTO locationSchemeSectorsDTO = new LocationSchemeSectorsDTO(sectors, locationScheme);
        when(locationSchemeService.save(any(LocationScheme.class)))
                .thenThrow(LocationSchemeAlreadyExists.class);
        testRestTemplate.withBasicAuth("admin", "password").postForEntity(URL_PREFIX, locationSchemeSectorsDTO, LocationSchemeSectorsDTO.class);

        verify(locationSchemeService, times(1)).save(any(LocationScheme.class));
    }
}