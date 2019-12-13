package nvt.kts.ticketapp.repository.sector;

import nvt.kts.ticketapp.domain.model.location.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LocationSectorRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private LocationSectorRepository locationSectorRepository;

    private final Long LOCATION_SCHEME_ID = 1L;
    Sector sector;
    LocationScheme locationScheme;
    @Before
    public void setUp(){
        locationScheme = new LocationScheme("Scheme 1", "Scheme address");
        testEntityManager.persistAndFlush(locationScheme);

        sector = new Sector(10.0, 10.0, 10.0, 0.0, 120,
                0, 0, SectorType.PARTER, locationScheme);
        sector.setDeleted(false);
        testEntityManager.persistAndFlush(sector);


        Sector sector2 = new Sector(10.0, 10.0, 10.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, locationScheme);
        sector2.setDeleted(false);
        testEntityManager.persistAndFlush(sector2);

        Location location = new Location(locationScheme);
        testEntityManager.persistAndFlush(location);

        LocationSector locationSector = new LocationSector(sector, location, 150.0, 120, false);
        testEntityManager.persistAndFlush(locationSector);

        LocationSector locationSector2 = new LocationSector(sector2, location, 150.0, 100, false);
        testEntityManager.persistAndFlush(locationSector2);
    }


    @Test
    public void findAllBySectorId_Positive() {
        List<LocationSector> locationSectors = locationSectorRepository.findAllBySectorId(sector.getId());

        assertNotNull(locationSectors);
        assertEquals(1, locationSectors.size());
        assertEquals(120, locationSectors.get(0).getCapacity());
    }
}