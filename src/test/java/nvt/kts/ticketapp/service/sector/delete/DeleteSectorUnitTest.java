package nvt.kts.ticketapp.service.sector.delete;

import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeCanNotBeDeleted;
import nvt.kts.ticketapp.exception.sector.CanNotDeleteSchemeSectors;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.service.sector.SectorService;
import nvt.kts.ticketapp.service.sector.SectorServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteSectorUnitTest {

    private SectorService sectorService;

    @Mock
    private SectorRepository sectorRepository;

    @Mock
    private LocationSectorRepository locationSectorRepository;

    private final Long EXISTING_SECTOR_ID = 1L;
    private final Long EXISTING_SECTOR2_ID = 2L;
    private final Long EXISTING_SCHEME_ID = 1L;

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

        when(locationSectorRepository.findAllBySectorId(EXISTING_SECTOR_ID)).
                thenReturn(Arrays.asList(locationSector, locationSector2));


        Sector deletedSector = new Sector(10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, locationScheme);
        deletedSector.setDeleted(true);
        deletedSector.setId(EXISTING_SECTOR_ID);
        Sector deletedSector2 = new Sector(10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, locationScheme);
        deletedSector2.setDeleted(true);
        deletedSector.setId(EXISTING_SECTOR2_ID);

        when(sectorRepository.saveAll(anyList())).
                thenReturn(Arrays.asList(deletedSector, deletedSector2));


        sectorService = new SectorServiceImpl(sectorRepository, locationSectorRepository);
    }

    /**
     * Test deleting list of sectors
     *
     * @throws CanNotDeleteSchemeSectors
     */
    @Test
    public void delete_Positive() throws CanNotDeleteSchemeSectors {
        SectorDTO sector = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);
        sector.setId(EXISTING_SECTOR_ID);
        SectorDTO sector2 = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);
        sector2.setId(EXISTING_SECTOR2_ID);
        when(locationSectorRepository.findAllBySectorId(EXISTING_SECTOR_ID)).
                thenReturn(new ArrayList<>());
        when(locationSectorRepository.findAllBySectorId(EXISTING_SECTOR2_ID)).
                thenReturn(new ArrayList<>());
        List<SectorDTO> sectors = sectorService.delete(Arrays.asList(sector, sector2));

        assertNotNull(sectors);
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
        SectorDTO sector = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);
        sector.setId(EXISTING_SECTOR_ID);
        sectorService.delete(Arrays.asList(sector));
    }
}
