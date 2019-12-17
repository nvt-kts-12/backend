package nvt.kts.ticketapp.service.sector.locationSector;

import nvt.kts.ticketapp.domain.dto.event.LocationSectorsDTO;
import nvt.kts.ticketapp.domain.dto.location.LocationSectorDTO;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.exception.location.LocationNotFound;
import nvt.kts.ticketapp.exception.location.LocationSectorsDoesNotExistForLocation;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import nvt.kts.ticketapp.exception.sector.LocationSectorDoesNotExist;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.service.sector.LocationSectorService;
import nvt.kts.ticketapp.service.sector.LocationSectorServiceImpl;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationSectorServiceUnitTest {

    private LocationSectorService locationSectorService;
    @Mock
    private LocationSectorRepository locationSectorRepository;

    @Mock
    private LocationRepository locationRepository;
    @Mock
    private SectorRepository sectorRepository;

    private final Long EXISTING_SECTOR_ID = 1L;
    private final Long EXISTING_SECTOR2_ID = 2L;
    private final Long EXISTING_SCHEME_ID = 1L;
    private final Long EXISTING_LOCATION_ID = 1L;
    private final Long EXISTING_LOCATION2_ID = 2L;
    private final Long NONEXISTENT_LOCATION_ID = 5L;
    private final Long EXISTING_LOCATION_SECTOR_ID = 1L;

    private static LocationScheme locationScheme;
    private static Location location;
    private static Location location2;
    private static Sector sector;
    private static Sector sector2;
    private static LocationSector locationSector;
    private static LocationSector locationSector2;

    @Before
    public void init() {
        locationScheme = new LocationScheme("Scheme", "Address");
        locationScheme.setId(EXISTING_SCHEME_ID);

        location = new Location(locationScheme);
        location.setId(EXISTING_LOCATION_ID);
        location2 = new Location(locationScheme);
        location2.setId(EXISTING_LOCATION2_ID);

        sector = new Sector(10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, locationScheme);
        sector.setId(EXISTING_SECTOR_ID);
        sector2 = new Sector(10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, locationScheme);
        sector2.setId(EXISTING_SECTOR2_ID);

        locationSector = new LocationSector(sector, location, 150.00, 100, false);
        locationSector2 = new LocationSector(sector, location2, 250.00, 100, false);

        when(locationSectorRepository.findAllByLocationIdAndDeletedFalse(EXISTING_LOCATION_ID)).
                thenReturn(Arrays.asList(locationSector, locationSector2));
        when(locationRepository.findByIdAndDeletedFalse(EXISTING_LOCATION_ID)).thenReturn(Optional.of(location));
        when(locationRepository.findByIdAndDeletedFalse(EXISTING_LOCATION2_ID)).thenReturn(Optional.of(location2));
        when(sectorRepository.findByIdAndDeletedFalse(EXISTING_SECTOR_ID)).thenReturn(Optional.of(sector));
        when(sectorRepository.findByIdAndDeletedFalse(EXISTING_SECTOR2_ID)).thenReturn(Optional.of(sector2));
        when(locationSectorRepository.save(any(LocationSector.class))).thenReturn(locationSector);


        locationSectorService = new LocationSectorServiceImpl(locationSectorRepository, sectorRepository, locationRepository);
    }

    @Test
    public void saveAll_Positive() throws LocationNotFound, SectorNotFound {
        List<LocationSectorDTO> locationSectorsDTOS = locationSectorService
                .saveAll(ObjectMapperUtils.mapAll(Arrays.asList(locationSector, locationSector2), LocationSectorDTO.class));

        assertNotNull(locationSectorsDTOS);
        assertEquals(2, locationSectorsDTOS.size());
        verify(locationSectorRepository, times(2)).save(any(LocationSector.class));
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

    @Test
    public void getOne_Positive() throws LocationSectorDoesNotExist {
        when(locationSectorRepository.findById(EXISTING_LOCATION_SECTOR_ID)).thenReturn(java.util.Optional.ofNullable(locationSector));

        LocationSectorDTO sector = locationSectorService.getOne(EXISTING_SECTOR_ID);

        assertNotNull(sector);
        assertEquals(sector.getSectorId(), locationSector.getSector().getId());
        assertEquals(sector.getCapacity(), locationSector.getCapacity());
    }

    @Test(expected = LocationSectorDoesNotExist.class)
    public void getOne_Negative_LocationSectorDoesNotExist() throws LocationSectorDoesNotExist {
        when(locationSectorRepository.findById(NONEXISTENT_LOCATION_ID)).thenReturn(Optional.empty());

        locationSectorService.getOne(NONEXISTENT_LOCATION_ID);
    }
}