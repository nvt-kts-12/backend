package nvt.kts.ticketapp.service.sector.locationSector;

import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.exception.location.LocationSectorsDoesNotExistForLocation;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.service.sector.LocationSectorService;
import nvt.kts.ticketapp.service.sector.LocationSectorServiceImpl;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationSectorServiceTest {

    private LocationSectorService locationSectorService;
    @Mock
    private LocationSectorRepository locationSectorRepository;


    private final Long EXISTING_SECTOR_ID = 1L;
    private final Long EXISTING_SECTOR2_ID = 1L;
    private final Long EXISTING_SCHEME_ID = 1L;
    private final Long EXISTING_LOCATION_ID = 1L;
    private final Long NONEXISTENT_LOCATION_ID = 2L;

    @Before
    public void init() {
        LocationScheme locationScheme = new LocationScheme("Scheme", "Address");
        locationScheme.setId(EXISTING_SCHEME_ID);

        Location location = new Location(locationScheme);
        Location location2 = new Location(locationScheme);

        Sector sector = new Sector(10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, locationScheme);
        sector.setId(EXISTING_SECTOR_ID);
        Sector sector2 = new Sector(10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, locationScheme);
        sector2.setId(EXISTING_SECTOR2_ID);

        LocationSector locationSector = new LocationSector(sector, location, 150.00, 100, false);
        LocationSector locationSector2 = new LocationSector(sector, location2, 250.00, 100, false);

        when(locationSectorRepository.findAllByLocationIdAndDeletedFalse(EXISTING_LOCATION_ID)).
                thenReturn(Arrays.asList(locationSector, locationSector2));

        locationSectorService = new LocationSectorServiceImpl(locationSectorRepository);
    }

    @Test
    public void saveAll_Positive() {
        LocationScheme locationScheme = new LocationScheme("Scheme", "Address");
        locationScheme.setId(EXISTING_SCHEME_ID);

        Location location = new Location(locationScheme);
        Location location2 = new Location(locationScheme);

        Sector sector = new Sector(10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, locationScheme);
        sector.setId(EXISTING_SECTOR_ID);
        Sector sector2 = new Sector(10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, locationScheme);
        sector2.setId(EXISTING_SECTOR2_ID);

        LocationSector locationSector = new LocationSector(sector, location, 150.00, 100, false);
        LocationSector locationSector2 = new LocationSector(sector, location2, 250.00, 100, false);

        locationSectorService.saveAll(Arrays.asList(locationSector, locationSector2));

        verify(locationSectorRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void get_Positive() throws LocationSectorsDoesNotExistForLocation {
        List<LocationSector> locationSectors = locationSectorService.get(EXISTING_LOCATION_ID);
        assertNotNull(locationSectors);
        assertEquals(2, locationSectors.size());
    }

    @Test(expected = LocationSectorsDoesNotExistForLocation.class)
    public void get_Negative() throws LocationSectorsDoesNotExistForLocation {
        when(locationSectorRepository.findAllByLocationIdAndDeletedFalse(NONEXISTENT_LOCATION_ID)).
                thenReturn(new ArrayList<>());
        locationSectorService.get(NONEXISTENT_LOCATION_ID);
    }
}