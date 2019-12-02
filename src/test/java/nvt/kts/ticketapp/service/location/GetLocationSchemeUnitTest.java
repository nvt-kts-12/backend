package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetLocationSchemeUnitTest {

    private LocationSchemeService locationSchemeService;

    @Mock
    private LocationSchemeRepository locationSchemeRepository;
    @Mock
    private LocationRepository locationRepository;

    private final Long EXISTING_SCHEME_ID = 1L;
    private final Long EXISTING_SCHEME2_ID = 5L;
    private final Long NONEXISTENT_SCHEME_ID = 2L;

    @Before
    public void init() {

        LocationScheme existingScheme = new LocationScheme("Scheme 1", "Address 1");
        existingScheme.setId(EXISTING_SCHEME_ID);
        LocationScheme existingScheme2 = new LocationScheme("Scheme 2", "Address 2");
        existingScheme2.setId(EXISTING_SCHEME2_ID);

        when(locationSchemeRepository.findByIdAndDeletedFalse(EXISTING_SCHEME_ID)).thenReturn(Optional.of(existingScheme));
        when(locationSchemeRepository.findByIdAndDeletedFalse(EXISTING_SCHEME2_ID)).thenReturn(Optional.of(existingScheme2));
        when(locationSchemeRepository.findByIdAndDeletedFalse(NONEXISTENT_SCHEME_ID)).thenReturn(Optional.empty());
        when(locationSchemeRepository.findAllByDeletedFalse()).thenReturn(Arrays.asList(existingScheme, existingScheme2));


        locationSchemeService = new LocationSchemeServiceImpl(locationSchemeRepository, locationRepository);
    }

    @Test
    public void getSchemeDto_Positive() throws LocationSchemeDoesNotExist {
        LocationSchemeDTO locationScheme = locationSchemeService.get(EXISTING_SCHEME_ID);
        assertNotNull(locationScheme);
        assertEquals(EXISTING_SCHEME_ID, locationScheme.getId());
    }

    @Test(expected = LocationSchemeDoesNotExist.class)
    public void getSchemeDto_Negative() throws LocationSchemeDoesNotExist {
        locationSchemeService.get(NONEXISTENT_SCHEME_ID);
    }

    @Test
    public void getScheme_Positive() throws LocationSchemeDoesNotExist {
        LocationScheme locationScheme = locationSchemeService.getScheme(EXISTING_SCHEME_ID);
        assertNotNull(locationScheme);
        assertEquals(EXISTING_SCHEME_ID, locationScheme.getId());
    }

    @Test(expected = LocationSchemeDoesNotExist.class)
    public void getScheme_Negative() throws LocationSchemeDoesNotExist {
        locationSchemeService.getScheme(NONEXISTENT_SCHEME_ID);
    }

    @Test
    public void getAll_Positive() {
        List<LocationSchemeDTO> locationSchemes = locationSchemeService.getAll();

        assertNotNull(locationSchemes);
        assertEquals(EXISTING_SCHEME_ID, locationSchemes.get(0).getId());
        assertEquals(EXISTING_SCHEME2_ID, locationSchemes.get(1).getId());
    }
}
