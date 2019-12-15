package nvt.kts.ticketapp.service.sector.locationSector;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.exception.location.LocationSectorsDoesNotExistForLocation;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.service.sector.LocationSectorService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationSectorServiceIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private LocationSectorService locationSectorService;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private LocationSectorRepository locationSectorRepository;

    @Autowired
    private LocationSchemeRepository locationSchemeRepository;

    @Autowired
    private LocationRepository locationRepository;

    /*Spens*/
    private static LocationScheme spensScheme;
    private static Location spens;
    private static Sector westGrandstand;
    private static Sector eastGrandstand;
    private static LocationSector west;
    private static LocationSector east;

    /*Marakana*/
    private static LocationScheme marakanaScheme;
    private static Location marakana;
    private static Sector northGrandstand;
    private static LocationSector north;

    private final Long NONEXISTENT_SECTOR_ID = 5L;

    @Before
    public void setUp() {
        spensScheme = locationSchemeRepository.save(new LocationScheme("Spens", "Maksima Gorkog"));
        spens = locationRepository.save(new Location(spensScheme));

        westGrandstand = sectorRepository.save(new Sector(10.0, 10.0, 15.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, spensScheme));
        eastGrandstand = sectorRepository.save(new Sector(0.0, 10.0, 5.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, spensScheme));

        west = new LocationSector(westGrandstand, spens, 270.0, 50, true);
        east = new LocationSector(eastGrandstand, spens, 200.0, 100, false);


        marakanaScheme = locationSchemeRepository.save(new LocationScheme("Stadion Rajko Mitic", "Ljutice Bogdana"));
        marakana = locationRepository.save(new Location(marakanaScheme));

        northGrandstand = sectorRepository.save(new Sector(10.0, 10.0, 15.0, 0.0, 12000,
                120, 100, SectorType.GRANDSTAND, marakanaScheme));
        north = new LocationSector(northGrandstand, marakana, 100.0, 12000, false);

        locationSectorRepository.save(north);
    }


    /**
     * Test saving list of location sectors
     */
    @Test
    public void saveAll_Positive() throws LocationSectorsDoesNotExistForLocation {
        locationSectorService.saveAll(Arrays.asList(west, east));

        List<LocationSector> spensSectors = locationSectorService.get(spens.getId());

        assertNotNull(spensSectors);
        assertEquals(2, spensSectors.size());
        assertEquals(west.getSector().getId(), spensSectors.get(0).getSector().getId());
        assertEquals(east.getSector().getId(), spensSectors.get(1).getSector().getId());
    }

    /**
     * Test getting existing location sector
     *
     * @throws LocationSectorsDoesNotExistForLocation
     */
    @Test
    public void get_Positive() throws LocationSectorsDoesNotExistForLocation {
    }

    /**
     * Test getting nonexistent location sector
     *
     * @throws LocationSectorsDoesNotExistForLocation
     */
    @Test(expected = LocationSectorsDoesNotExistForLocation.class)
    public void get_Negative() throws LocationSectorsDoesNotExistForLocation {
    }
}
