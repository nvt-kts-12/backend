package nvt.kts.ticketapp.service.sector.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeCanNotBeDeleted;
import nvt.kts.ticketapp.exception.sector.CanNotDeleteSchemeSectors;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.service.sector.SectorService;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteSectorIntegrationTest {


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

    /*Marakana*/
    private static LocationScheme marakanaScheme;
    private static Sector northGrandstand;

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

        locationSectorRepository.save(west);


        marakanaScheme = locationSchemeRepository.save(new LocationScheme("Stadion Rajko Mitic", "Ljutice Bogdana"));
        northGrandstand = sectorRepository.save(new Sector(10.0, 10.0, 15.0, 0.0, 12000,
                120, 100, SectorType.GRANDSTAND, marakanaScheme));
    }


    /**
     * Test deleting list of sectors
     *
     * @throws CanNotDeleteSchemeSectors
     */
    @Test
    public void delete_Positive() throws CanNotDeleteSchemeSectors {
        SectorDTO eastDto = ObjectMapperUtils.map(eastGrandstand, SectorDTO.class);
        SectorDTO northDto = ObjectMapperUtils.map(northGrandstand, SectorDTO.class);
        List<SectorDTO> sectors = sectorService.delete(Arrays.asList(eastDto, northDto));

        assertNotNull(sectors);
        assertEquals(2, sectors.size());
        assertTrue(sectors.get(0).isDeleted());
        assertTrue(sectors.get(1).isDeleted());
    }

    /**
     * Test deleting list of sectors that are assigned to an event
     *
     * @throws CanNotDeleteSchemeSectors
     */
    @Test(expected = CanNotDeleteSchemeSectors.class)
    public void delete_Negative_CanNotDeleteSchemeSectors() throws CanNotDeleteSchemeSectors {
        SectorDTO eastDto = ObjectMapperUtils.map(eastGrandstand, SectorDTO.class);
        SectorDTO westDto = ObjectMapperUtils.map(westGrandstand, SectorDTO.class);

        sectorService.delete(Arrays.asList(eastDto, westDto));
    }
}
