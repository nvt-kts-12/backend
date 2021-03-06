package nvt.kts.ticketapp.service.location.delete;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeCanNotBeDeleted;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import nvt.kts.ticketapp.service.location.LocationSchemeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteLocationSchemeUnitTest {

    private LocationSchemeService locationSchemeService;

    @Mock
    private LocationSchemeRepository locationSchemeRepository;
    @Mock
    private LocationRepository locationRepository;

    private final Long EXISTING_SCHEME_ID = 1L;
    private final Long NONEXISTENT_SCHEME_ID = 5L;
    private final Long EXISTING_LOCATION_ID = 1L;
    private final Long EXISTING_LOCATION2_ID = 2L;

    @Before
    public void init() {
        LocationScheme locationScheme = new LocationScheme("Scheme 1", "Address 1");
        locationScheme.setId(EXISTING_SCHEME_ID);
        LocationScheme deletedLocationScheme = new LocationScheme("Scheme 1", "Address 1");
        deletedLocationScheme.setId(EXISTING_SCHEME_ID);
        deletedLocationScheme.setDeleted(true);

        Location location = new Location(locationScheme);
        location.setId(EXISTING_LOCATION_ID);
        Location location2 = new Location(locationScheme);
        location2.setId(EXISTING_LOCATION2_ID);
        when(locationRepository.findAllBySchemeIdAndDeletedFalse(EXISTING_SCHEME_ID)).
                thenReturn(Arrays.asList(location, location2));

        when(locationSchemeRepository.findByIdAndDeletedFalse(EXISTING_SCHEME_ID)).
                thenReturn(Optional.of(locationScheme));
        when(locationSchemeRepository.save(any(LocationScheme.class))).
                thenReturn(deletedLocationScheme);
        locationSchemeService = new LocationSchemeServiceImpl(locationSchemeRepository, locationRepository);
    }

    /**
     * Test deleting location scheme with EXISTING_SCHEME_ID == 1L
     *
     * @throws LocationSchemeDoesNotExist
     * @throws LocationSchemeCanNotBeDeleted
     */
    @Test
    public void deleteScheme_Positive() throws LocationSchemeDoesNotExist, LocationSchemeCanNotBeDeleted {
        when(locationRepository.findAllBySchemeIdAndDeletedFalse(EXISTING_SCHEME_ID)).
                thenReturn(new ArrayList<>());
        LocationSchemeDTO locationScheme = locationSchemeService.delete(EXISTING_SCHEME_ID);

        assertNotNull(locationScheme);
        assertTrue(locationScheme.isDeleted());
        assertEquals(EXISTING_SCHEME_ID, locationScheme.getId());

        verify(locationSchemeRepository, times(1)).findByIdAndDeletedFalse(EXISTING_SCHEME_ID);
        verify(locationRepository, times(1)).findAllBySchemeIdAndDeletedFalse(EXISTING_SCHEME_ID);
        verify(locationSchemeRepository, times(1)).save(any(LocationScheme.class));
    }

    /**
     * Test deleting scheme that already has location created and assigned to event, fails
     *
     * @throws LocationSchemeDoesNotExist
     * @throws LocationSchemeCanNotBeDeleted
     */
    @Test(expected = LocationSchemeCanNotBeDeleted.class)
    public void deleteScheme_Negative_CanNotBeDeleted() throws LocationSchemeDoesNotExist, LocationSchemeCanNotBeDeleted {
        locationSchemeService.delete(EXISTING_SCHEME_ID);

        verify(locationSchemeRepository, times(1)).findByIdAndDeletedFalse(EXISTING_SCHEME_ID);
        verify(locationRepository, times(1)).findAllBySchemeIdAndDeletedFalse(EXISTING_SCHEME_ID);
        verify(locationSchemeRepository, times(0)).save(any(LocationScheme.class));
    }

    /**
     * Test deleting scheme with NONEXISTENT_SCHEME_ID == 5L, fails because such scheme does not exist
     * @throws LocationSchemeDoesNotExist
     * @throws LocationSchemeCanNotBeDeleted
     */
    @Test(expected = LocationSchemeDoesNotExist.class)
    public void deleteScheme_Negative_DoesNotExist() throws LocationSchemeDoesNotExist, LocationSchemeCanNotBeDeleted {
        locationSchemeService.delete(NONEXISTENT_SCHEME_ID);

        verify(locationSchemeRepository, times(1)).findByIdAndDeletedFalse(NONEXISTENT_SCHEME_ID);
        verify(locationRepository, times(0)).findAllBySchemeIdAndDeletedFalse(anyLong());
        verify(locationSchemeRepository, times(0)).save(any(LocationScheme.class));
    }
}
