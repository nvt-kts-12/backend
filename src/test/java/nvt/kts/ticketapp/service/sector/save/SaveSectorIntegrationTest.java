package nvt.kts.ticketapp.service.sector.save;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.SectorType;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SaveSectorIntegrationTest {

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

    private static SectorDTO sector;
    private static SectorDTO sector2;
    private static SectorDTO sector3;

    private static LocationScheme locationScheme;
    @Before
    public void setUp() {
        sector = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);
        sector2 = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);
        sector3 = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                0, 0, SectorType.PARTER);

        locationScheme = locationSchemeRepository.save(new LocationScheme("Scheme", "Address"));
    }

    @Test
    public void saveAll_Positive() {
        List<SectorDTO> sectors = sectorService.saveAll(Arrays.asList(sector, sector2, sector3), locationScheme);

        assertNotNull(sectors);
        assertEquals(3, sectors.size());
    }
}
