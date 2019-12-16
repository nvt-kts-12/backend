package nvt.kts.ticketapp.service.sector;


import nvt.kts.ticketapp.service.sector.delete.DeleteSectorIntegrationTest;
import nvt.kts.ticketapp.service.sector.get.GetSectorIntegrationTest;
import nvt.kts.ticketapp.service.sector.save.SaveSectorIntegrationTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GetSectorIntegrationTest.class,
        DeleteSectorIntegrationTest.class,
        SaveSectorIntegrationTest.class
})
public class SectorServiceIntegrationSuite {
}
