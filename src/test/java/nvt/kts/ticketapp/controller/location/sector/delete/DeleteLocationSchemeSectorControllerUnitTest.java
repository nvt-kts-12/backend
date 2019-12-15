package nvt.kts.ticketapp.controller.location.sector.delete;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.dto.location.LocationSchemeSectorsDTO;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.SectorType;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeCanNotBeDeleted;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.exception.sector.CanNotDeleteSchemeSectors;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class DeleteLocationSchemeSectorControllerUnitTest {

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
    private final Long NONEXISTENT_SCHEME_ID = 2L;
    private final Long BOOKED_SCHEME_ID = 3L;

    private SectorDTO sector;
    private SectorDTO sector2;
    private SectorDTO sector3;
    private SectorDTO bookedSector;

    @Before
    public void init() throws CanNotDeleteSchemeSectors, LocationSchemeDoesNotExist, LocationSchemeCanNotBeDeleted {
        sector = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);
        sector2 = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);
        sector3 = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                0, 0, SectorType.PARTER);
        bookedSector = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                0, 0, SectorType.PARTER);

        LocationSchemeDTO deletedScheme = new LocationSchemeDTO(true, "Scheme", "Address");
        deletedScheme.setId(EXISTING_SCHEME_ID);

        SectorDTO deletedSector = new SectorDTO(true, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);
        SectorDTO deletedSector2 = new SectorDTO(true, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);
        SectorDTO deletedSector3 = new SectorDTO(true, 10.0, 10.0,
                0.0, 0.0, 100,
                0, 0, SectorType.PARTER);
        List<SectorDTO> deletedSectors = Arrays.asList(deletedSector, deletedSector2, deletedSector3);


        when(sectorService.delete(anyList())).thenReturn(deletedSectors);
        when(locationSchemeService.delete(EXISTING_SCHEME_ID)).thenReturn(deletedScheme);
        when(locationSchemeService.delete(NONEXISTENT_SCHEME_ID)).thenThrow(LocationSchemeDoesNotExist.class);
        when(locationSchemeService.delete(BOOKED_SCHEME_ID)).thenThrow(LocationSchemeCanNotBeDeleted.class);
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

        List<SectorDTO> sectorsToDelete = Arrays.asList(sector, sector2, sector3);
        LocationSchemeDTO locationScheme = new LocationSchemeDTO(false, "Scheme", "Address");
        locationScheme.setId(EXISTING_SCHEME_ID);

        LocationSchemeSectorsDTO locationSchemeSectorsDTO = new LocationSchemeSectorsDTO(sectorsToDelete, locationScheme);

        ResponseEntity<LocationSchemeSectorsDTO> response =
                testRestTemplate.withBasicAuth("admin", "password").exchange(URL_PREFIX, HttpMethod.DELETE,
                        new HttpEntity<LocationSchemeSectorsDTO>(locationSchemeSectorsDTO), LocationSchemeSectorsDTO.class);


        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EXISTING_SCHEME_ID, response.getBody().getLocationScheme().getId());
        assertEquals(3, response.getBody().getSectors().size());
        assertTrue(response.getBody().getSectors().get(0).isDeleted());
        assertTrue(response.getBody().getSectors().get(1).isDeleted());
        assertTrue(response.getBody().getSectors().get(2).isDeleted());

        verify(sectorService, times(1)).delete(anyList());
        verify(locationSchemeService, times(1)).delete(any(Long.class));
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
        List<SectorDTO> sectorsToDelete = Arrays.asList(sector, sector2, sector3);
        LocationSchemeDTO locationScheme = new LocationSchemeDTO(false, "Scheme", "Address");
        locationScheme.setId(NONEXISTENT_SCHEME_ID);

        LocationSchemeSectorsDTO locationSchemeSectorsDTO = new LocationSchemeSectorsDTO(sectorsToDelete, locationScheme);

        ResponseEntity<LocationSchemeSectorsDTO> response =
                testRestTemplate.withBasicAuth("admin", "password").exchange(URL_PREFIX, HttpMethod.DELETE,
                        new HttpEntity<LocationSchemeSectorsDTO>(locationSchemeSectorsDTO), LocationSchemeSectorsDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verify(sectorService, times(1)).delete(anyList());
        verify(locationSchemeService, times(1)).delete(anyLong());
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
        List<SectorDTO> sectorsToDelete = Arrays.asList(sector, sector2, sector3, bookedSector);

        when(sectorService.delete(anyList())).thenThrow(CanNotDeleteSchemeSectors.class);

        LocationSchemeDTO locationScheme = new LocationSchemeDTO(false, "Scheme", "Address");
        locationScheme.setId(EXISTING_SCHEME_ID);

        LocationSchemeSectorsDTO locationSchemeSectorsDTO = new LocationSchemeSectorsDTO(sectorsToDelete, locationScheme);

        ResponseEntity<LocationSchemeSectorsDTO> response =
                testRestTemplate.withBasicAuth("admin", "password").exchange(URL_PREFIX, HttpMethod.DELETE,
                        new HttpEntity<LocationSchemeSectorsDTO>(locationSchemeSectorsDTO), LocationSchemeSectorsDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verify(sectorService, times(1)).delete(anyList());
        verify(locationSchemeService, times(0)).delete(anyLong());
    }

    /**
     * Test deleting location scheme that is booked on some event
     * @throws CanNotDeleteSchemeSectors
     * @throws LocationSchemeDoesNotExist
     * @throws LocationSchemeCanNotBeDeleted
     */
    @Test
    public void delete_Negative_LocationSchemeCanNotBeDeleted() throws CanNotDeleteSchemeSectors, LocationSchemeDoesNotExist, LocationSchemeCanNotBeDeleted {
        List<SectorDTO> sectorsToDelete = Arrays.asList(sector, sector2, sector3);

        LocationSchemeDTO locationScheme = new LocationSchemeDTO(false, "Scheme", "Address");
        locationScheme.setId(BOOKED_SCHEME_ID);

        LocationSchemeSectorsDTO locationSchemeSectorsDTO = new LocationSchemeSectorsDTO(sectorsToDelete, locationScheme);

        ResponseEntity<LocationSchemeSectorsDTO> response =
                testRestTemplate.withBasicAuth("admin", "password").exchange(URL_PREFIX, HttpMethod.DELETE,
                        new HttpEntity<LocationSchemeSectorsDTO>(locationSchemeSectorsDTO), LocationSchemeSectorsDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verify(sectorService, times(1)).delete(anyList());
        verify(locationSchemeService, times(1)).delete(anyLong());
    }
}
