package nvt.kts.ticketapp.service.location.save;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeAlreadyExists;
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

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SaveLocationSchemeUnitTest {

    private LocationSchemeService locationSchemeService;

    @Mock
    private LocationSchemeRepository locationSchemeRepository;

    @Mock
    private LocationRepository locationRepository;

    private final Long EXISTING_SCHEME_ID = 1L;
    private final Long CREATED_SCHEME_ID = 2L;

    private final String EXISTING_SCHEME_NAME = "Scheme 1";
    private final String NONEXISTENT_SCHEME_NAME = "Scheme 2";
    private final String UPDATED_SCHEME_NAME = "Updated scheme";


    @Before
    public void init() {
        LocationScheme existingScheme = new LocationScheme(EXISTING_SCHEME_NAME, "Address 1");
        existingScheme.setId(EXISTING_SCHEME_ID);

        when(locationSchemeRepository.findByNameIgnoreCaseAndDeletedFalse(EXISTING_SCHEME_NAME)).
                thenReturn(Optional.of(existingScheme));

        when(locationSchemeRepository.findByNameIgnoreCaseAndDeletedFalse(NONEXISTENT_SCHEME_NAME)).
                thenReturn(Optional.empty());


        locationSchemeService = new LocationSchemeServiceImpl(locationSchemeRepository, locationRepository);
    }

    /**
     * Test saving a scheme without id and with unique name, that means saving a new location scheme
     *
     * @throws LocationSchemeAlreadyExists
     */
    @Test
    public void saveNewScheme_Positive() throws LocationSchemeAlreadyExists {
        LocationScheme nonExistentScheme = new LocationScheme(NONEXISTENT_SCHEME_NAME, "Address 2");
        LocationScheme createdScheme = new LocationScheme(NONEXISTENT_SCHEME_NAME, "Address 2");
        createdScheme.setId(CREATED_SCHEME_ID);

        when(locationSchemeRepository.save(nonExistentScheme)).thenReturn(createdScheme);

        LocationSchemeDTO locationScheme = locationSchemeService.save(nonExistentScheme);

        assertNotNull(locationScheme);
        assertEquals(CREATED_SCHEME_ID, locationScheme.getId());
        assertEquals(NONEXISTENT_SCHEME_NAME, locationScheme.getName());

        verify(locationSchemeRepository, times(1)).findByNameIgnoreCaseAndDeletedFalse(NONEXISTENT_SCHEME_NAME);
        verify(locationSchemeRepository, times(1)).save(nonExistentScheme);
    }

    /**
     * Test saving new location scheme without id and with existing name,
     * fails because there can not be two schemas with same name
     *
     * @throws LocationSchemeAlreadyExists
     */
    @Test(expected = LocationSchemeAlreadyExists.class)
    public void saveNewScheme_Negative_LocationSchemeAlreadyExists() throws LocationSchemeAlreadyExists {
        LocationScheme existentScheme = new LocationScheme(EXISTING_SCHEME_NAME, "Address 1");
        locationSchemeService.save(existentScheme);

        verify(locationSchemeRepository, times(1)).findByNameIgnoreCaseAndDeletedFalse(NONEXISTENT_SCHEME_NAME);
        verify(locationSchemeRepository, times(0)).save(any(LocationScheme.class));
    }

    /**
     * Test saving scheme with existing id, that means updating
     */
    @Test
    public void updateScheme_Positive() {
        LocationScheme updatedScheme = new LocationScheme(UPDATED_SCHEME_NAME, "Address 1");
        updatedScheme.setId(EXISTING_SCHEME_ID);

        when(locationSchemeRepository.save(any(LocationScheme.class))).thenReturn(updatedScheme);
        LocationScheme locationScheme = locationSchemeRepository.save(updatedScheme);

        assertNotNull(locationScheme);
        assertEquals(UPDATED_SCHEME_NAME, locationScheme.getName());
        assertEquals(EXISTING_SCHEME_ID, locationScheme.getId());

        verify(locationSchemeRepository, times(0)).findByNameIgnoreCaseAndDeletedFalse(anyString());
        verify(locationSchemeRepository, times(1)).save(any(LocationScheme.class));
    }

}