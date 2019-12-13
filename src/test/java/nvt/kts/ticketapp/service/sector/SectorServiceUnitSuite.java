package nvt.kts.ticketapp.service.sector;

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
