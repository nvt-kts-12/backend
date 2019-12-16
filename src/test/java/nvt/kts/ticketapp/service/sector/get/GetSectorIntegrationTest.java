package nvt.kts.ticketapp.service.sector.get;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.service.sector.SectorService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetSectorIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private SectorService sectorService;

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

        locationSectorRepository.save(west);
        locationSectorRepository.save(east);


        marakanaScheme = locationSchemeRepository.save(new LocationScheme("Stadion Rajko Mitic", "Ljutice Bogdana"));
        marakana = locationRepository.save(new Location(marakanaScheme));

        northGrandstand = sectorRepository.save(new Sector(10.0, 10.0, 15.0, 0.0, 12000,
                120, 100, SectorType.GRANDSTAND, marakanaScheme));
        north = new LocationSector(northGrandstand, marakana, 100.0, 12000, false);

        locationSectorRepository.save(north);
    }


    /**
     * Test getting existing sector
     *
     * @throws SectorDoesNotExist
     */
    @Test
    public void get_Positive() throws SectorDoesNotExist {
        SectorDTO providedWest = sectorService.get(westGrandstand.getId());

        assertNotNull(providedWest);
        assertEquals(westGrandstand.getId(), providedWest.getId());
        assertEquals(westGrandstand.getCapacity(), providedWest.getCapacity());
        assertEquals(westGrandstand.getType(), providedWest.getType());
        assertEquals(westGrandstand.getTopLeftX(), providedWest.getTopLeftX());
    }

    /**
     * Test getting nonexistent sectorDTO
     *
     * @throws SectorDoesNotExist
     */
    @Test(expected = SectorDoesNotExist.class)
    public void get_Negative_SectorDoesNotExist() throws SectorDoesNotExist {
        sectorService.get(NONEXISTENT_SECTOR_ID);
    }

    /**
     * Test getting all sectors
     */
    @Test
    public void getAll_Positive() {
        List<SectorDTO> allSectors = sectorService.getAll();

        assertNotNull(allSectors);
        assertEquals(3, allSectors.size());
        assertEquals(westGrandstand.getId(), allSectors.get(0).getId());
        assertEquals(eastGrandstand.getId(), allSectors.get(1).getId());
        assertEquals(northGrandstand.getId(), allSectors.get(2).getId());
    }

    /**
     * Test getting all sectors by location scheme
     */
    @Test
    public void getByScheme_Positive() {
        List<SectorDTO> spensSectors = sectorService.getByScheme(spensScheme.getId());

        assertNotNull(spensSectors);
        assertEquals(2, spensSectors.size());
        assertEquals(westGrandstand.getId(), spensSectors.get(0).getId());
        assertEquals(eastGrandstand.getId(), spensSectors.get(1).getId());
    }

    /**
     * Test getting sector
     *
     * @throws SectorDoesNotExist
     */
    @Test
    public void getSector_Positive() throws SectorDoesNotExist {
        Sector providedMarakana = sectorService.getSector(northGrandstand.getId());

        assertNotNull(providedMarakana);
        assertEquals(northGrandstand.getId(), providedMarakana.getId());
        assertEquals(northGrandstand.getLocationScheme().getId(), providedMarakana.getLocationScheme().getId());
    }

    /**
     * Test getting sector with NONEXISTENT_SECTOR_ID == 2L, fails
     *
     * @throws SectorDoesNotExist
     */
    @Test(expected = SectorDoesNotExist.class)
    public void getSector_Negative_SectorDoesNotExist() throws SectorDoesNotExist {
        sectorService.getSector(NONEXISTENT_SECTOR_ID);
    }
}
