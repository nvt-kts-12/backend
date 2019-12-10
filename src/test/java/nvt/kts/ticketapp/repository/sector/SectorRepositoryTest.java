package nvt.kts.ticketapp.repository.sector;

import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.domain.model.location.SectorType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SectorRepositoryTest {


    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private SectorRepository sectorRepository;

//    private final Long EXISTING_SECTOR_ID = 1L;
//    private final Long EXISTING_SCHEME_ID = 2L;

    Sector existingSector;
    LocationScheme existingLocationScheme;
    @Before
    public void setUp() throws Exception {
        existingLocationScheme = new LocationScheme("Scheme 1", "Scheme address 1");
        testEntityManager.persistAndFlush(existingLocationScheme);

        existingSector = new Sector(10.0, 10.0, 10.0, 0.0, 150,
                0, 0, SectorType.PARTER, existingLocationScheme);
        existingSector.setDeleted(false);
        testEntityManager.persist(existingSector);

        Sector sector2 = new Sector(20.0, 20.0, 10.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, existingLocationScheme);
        sector2.setDeleted(false);
        testEntityManager.persist(sector2);

        Sector sector3 = new Sector(30.0, 30.0, 10.0, 0.0, 150,
                0, 0, SectorType.PARTER, existingLocationScheme);
        sector2.setDeleted(true);
        testEntityManager.persist(sector3);

        testEntityManager.flush();
    }

    @Test
    public void findByIdAndDeletedFalse_Positive() {
        Optional<Sector> sector = sectorRepository.findByIdAndDeletedFalse(existingSector.getId());

        assertNotNull(sector.get());
        assertEquals(150, sector.get().getCapacity());
        assertEquals(SectorType.PARTER, sector.get().getType());
        assertFalse(sector.get().isDeleted());
    }

    @Test
    public void findAllByDeletedFalse_Positive() {
        List<Sector> sectors = sectorRepository.findAllByDeletedFalse();

        assertNotNull(sectors);
        assertEquals(2, sectors.size());
        assertFalse(sectors.get(0).isDeleted());
        assertFalse(sectors.get(1).isDeleted());
    }

    @Test
    public void findAllByLocationSchemeIdAndDeletedFalse_Positive() {
        List<Sector> sectors = sectorRepository.findAllByLocationSchemeIdAndDeletedFalse(existingLocationScheme.getId());

        assertNotNull(sectors);
        assertEquals(2, sectors.size());
        assertFalse(sectors.get(0).isDeleted());
        assertFalse(sectors.get(1).isDeleted());
    }
}