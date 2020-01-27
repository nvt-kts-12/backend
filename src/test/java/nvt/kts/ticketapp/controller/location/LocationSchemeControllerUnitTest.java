package nvt.kts.ticketapp.controller.location;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import nvt.kts.ticketapp.service.location.LocationSchemeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class LocationSchemeControllerUnitTest {

    @LocalServerPort
    int randomServerPort;

    private static final String URL_PREFIX = "/api/locationScheme";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private LocationSchemeServiceImpl locationSchemeService;


    private final Long EXISTING_SCHEME_ID = 1L;
    private final Long EXISTING_SCHEME2_ID = 3L;
    private final Long NONEXISTENT_SCHEME_ID = 2L;

    @Before
    public void init() throws LocationSchemeDoesNotExist {
        LocationSchemeDTO locationSchemeDTO = new LocationSchemeDTO(false, "Scheme", "Address");
        locationSchemeDTO.setId(EXISTING_SCHEME_ID);
        LocationSchemeDTO locationScheme2DTO = new LocationSchemeDTO(false, "Scheme", "Address");
        locationScheme2DTO.setId(EXISTING_SCHEME2_ID);


        when(locationSchemeService.get(EXISTING_SCHEME_ID)).thenReturn(locationSchemeDTO);
        when(locationSchemeService.get(NONEXISTENT_SCHEME_ID)).thenThrow(LocationSchemeDoesNotExist.class);

        when(locationSchemeService.getAll()).thenReturn(Arrays.asList(locationSchemeDTO, locationScheme2DTO));
    }

    /**
     * Test getting LocationScheme with id EXISTING_SCHEME_ID == 1L
     *
     * @throws LocationSchemeDoesNotExist
     */
    @Test
    public void get_Positive() throws LocationSchemeDoesNotExist {
        ResponseEntity<LocationSchemeDTO> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity(URL_PREFIX + "/1", LocationSchemeDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(EXISTING_SCHEME_ID, response.getBody().getId());
    }

    /**
     * Test getting LocationScheme with id NONEXISTENT_SCHEME_ID == 2L fails
     *
     * @throws LocationSchemeDoesNotExist
     */
    @Test
    public void get_Negative() throws LocationSchemeDoesNotExist {
        ResponseEntity<LocationSchemeDTO> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity(URL_PREFIX + "/2", LocationSchemeDTO.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test getting all location schemas
     */
    @Test
    public void getAll_Positive() {
        ResponseEntity<List<LocationSchemeDTO>> response = testRestTemplate.withBasicAuth("username", "password")
                .exchange(URL_PREFIX +"/getAll", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<LocationSchemeDTO>>() {
                });

        assertNotNull(response.getBody());
        assertEquals(EXISTING_SCHEME_ID, response.getBody().get(0).getId());

        verify(locationSchemeService, times(1)).getAll();
    }
}