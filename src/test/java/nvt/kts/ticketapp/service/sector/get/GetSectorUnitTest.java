package nvt.kts.ticketapp.service.sector.get;

import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.domain.model.location.SectorType;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
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
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetSectorUnitTest {

    private SectorService sectorService;

    @Mock
    private SectorRepository sectorRepository;
    @Mock
    private LocationSectorRepository locationSectorRepository;

    private final Long EXISTING_SECTOR_ID = 1L;
    private final Long EXISTING_SECTOR2_ID = 1L;
    private final Long NONEXISTENT_SECTOR_ID = 2L;
    private final Long EXISTING_SCHEME_ID = 1L;

    @Before
    public void init() {
        LocationScheme locationScheme = new LocationScheme("Scheme", "Address");
        locationScheme.setId(EXISTING_SCHEME_ID);
        Sector sector = new Sector(10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, locationScheme);
        sector.setId(EXISTING_SECTOR_ID);
        Sector sector2 = new Sector(10.0, 10.0,
                0.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, locationScheme);
        sector2.setId(EXISTING_SECTOR2_ID);


        when(sectorRepository.findAllByLocationSchemeIdAndDeletedFalse(EXISTING_SCHEME_ID)).
                thenReturn(Arrays.asList(sector, sector2));
        when(sectorRepository.findByIdAndDeletedFalse(EXISTING_SECTOR_ID)).
                thenReturn(Optional.of(sector));
        when(sectorRepository.findAllByDeletedFalse()).
                thenReturn(Arrays.asList(sector, sector2));
        sectorService = new SectorServiceImpl(sectorRepository, locationSectorRepository);
    }

    /**
     * Test getting sector with EXISTING_SECTOR_ID == 1L
     *
     * @throws SectorDoesNotExist
     */
    @Test
    public void get_Positive() throws SectorDoesNotExist {
        SectorDTO sectorDTO = sectorService.get(EXISTING_SECTOR_ID);

        assertNotNull(sectorDTO);
        assertEquals(EXISTING_SCHEME_ID, sectorDTO.getId());
        assertEquals(10.0, sectorDTO.getTopLeftX(), 0.001);
        assertEquals(10.0, sectorDTO.getTopLeftY(), 0.001);
        assertEquals(0.0, sectorDTO.getBottomRightX(), 0.001);
        assertEquals(0.0, sectorDTO.getBottomRightY(), 0.001);
        assertEquals(100, sectorDTO.getCapacity());
        assertEquals(10, sectorDTO.getRowNum());
        assertEquals(10, sectorDTO.getColNum());

        verify(sectorRepository, times(1)).findByIdAndDeletedFalse(anyLong());
    }

    /**
     * Test getting sectorDTO with NONEXISTENT_SECTOR_ID == 2L
     *
     * @throws SectorDoesNotExist
     */
    @Test(expected = SectorDoesNotExist.class)
    public void get_Negative_SectorDoesNotExist() throws SectorDoesNotExist {
        sectorService.get(NONEXISTENT_SECTOR_ID);

        verify(sectorRepository, times(1)).findByIdAndDeletedFalse(anyLong());
    }

    /**
     * Test getting all sectors
     */
    @Test
    public void getAll_Positive() {
        List<SectorDTO> sectors = sectorService.getAll();

        assertNotNull(sectors);
        assertEquals(2, sectors.size());

        verify(sectorRepository, times(1)).findAllByDeletedFalse();
    }

    /**
     * Test getting all sectors by location scheme
     */
    @Test
    public void getByScheme_Positive() {
        List<SectorDTO> sectors = sectorService.getByScheme(EXISTING_SCHEME_ID);

        assertNotNull(sectors);
        assertEquals(sectors.get(0).getId(), EXISTING_SECTOR_ID);
        assertEquals(sectors.get(0).getTopLeftX(), 10.0, 0.001);
        assertEquals(sectors.get(0).getTopLeftY(), 10.0, 0.001);
        assertEquals(sectors.get(0).getBottomRightX(), 0.0, 0.001);
        assertEquals(sectors.get(0).getBottomRightY(), 0.0, 0.001);
        assertEquals(sectors.get(0).getCapacity(), 100);
        assertEquals(sectors.get(0).getRowNum(), 10);
        assertEquals(sectors.get(0).getColNum(), 10);
        assertEquals(sectors.get(0).getType(), SectorType.GRANDSTAND);

        assertEquals(sectors.get(1).getId(), EXISTING_SECTOR2_ID);
        assertEquals(sectors.get(1).getTopLeftX(), 10.0, 0.001);
        assertEquals(sectors.get(1).getTopLeftY(), 10.0, 0.001);
        assertEquals(sectors.get(1).getBottomRightX(), 0.0, 0.001);
        assertEquals(sectors.get(1).getBottomRightY(), 0.0, 0.001);
        assertEquals(sectors.get(1).getCapacity(), 100);
        assertEquals(sectors.get(1).getRowNum(), 10);
        assertEquals(sectors.get(1).getColNum(), 10);
        assertEquals(sectors.get(1).getType(), SectorType.GRANDSTAND);

        verify(sectorRepository, times(1)).findAllByLocationSchemeIdAndDeletedFalse(anyLong());
    }

    /**
     * Test getting sector
     *
     * @throws SectorDoesNotExist
     */
    @Test
    public void getSector_Positive() throws SectorDoesNotExist {
        Sector sector = sectorService.getSector(EXISTING_SECTOR_ID);
        assertNotNull(sector);
        assertEquals(EXISTING_SECTOR_ID, sector.getId());
        assertEquals(10.0, sector.getTopLeftX(), 0.001);
        assertEquals(10.0, sector.getTopLeftY(), 0.001);
        assertEquals(0.0, sector.getBottomRightX(), 0.001);
        assertEquals(0.0, sector.getBottomRightY(), 0.001);
        assertEquals(100, sector.getCapacity());
        assertEquals(10, sector.getRowNum());
        assertEquals(10, sector.getColNum());
    }

    /**
     * Test getting sector with NONEXISTENT_SECTOR_ID == 2L, fails
     * @throws SectorDoesNotExist
     */
    @Test(expected = SectorDoesNotExist.class)
    public void getSector_Negative_SectorDoesNotExist() throws SectorDoesNotExist {
        sectorService.getSector(NONEXISTENT_SECTOR_ID);
    }
}
