package nvt.kts.ticketapp.service.sector.save;

import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.domain.model.location.SectorType;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SaveSectorUnitTest {

    private SectorService sectorService;

    @Mock
    private SectorRepository sectorRepository;

    @Mock
    private LocationSectorRepository locationSectorRepository;

    private final Long EXISTING_SCHEME_ID = 1L;

    @Before
    public void init() {
        sectorService = new SectorServiceImpl(sectorRepository, locationSectorRepository);
    }

    @Test
    public void saveAll_Positive() {

        SectorDTO sector = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);
        SectorDTO sector2 = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND);
        SectorDTO sector3 = new SectorDTO(false, 10.0, 10.0,
                0.0, 0.0, 100,
                0, 0, SectorType.PARTER);
        List<SectorDTO> sectors = Arrays.asList(sector, sector2, sector3);

        LocationScheme locationScheme = new LocationScheme("Scheme", "Address");
        locationScheme.setId(EXISTING_SCHEME_ID);

        sectorService.saveAll(sectors, locationScheme);
        verify(sectorRepository, times(3)).save(any(Sector.class));
    }
}