package nvt.kts.ticketapp.service.sector;

import nvt.kts.ticketapp.service.sector.delete.DeleteSectorUnitTest;
import nvt.kts.ticketapp.service.sector.get.GetSectorUnitTest;
import nvt.kts.ticketapp.service.sector.locationSector.LocationSectorServiceTest;
import nvt.kts.ticketapp.service.sector.save.SaveSectorUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SaveSectorUnitTest.class,
        GetSectorUnitTest.class,
        DeleteSectorUnitTest.class,
        LocationSectorServiceTest.class
})
public class SectorServiceUnitSuite {
}
