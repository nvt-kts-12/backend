package nvt.kts.ticketapp.service.sector;


import nvt.kts.ticketapp.service.sector.delete.DeleteSectorIntegrationTest;
import nvt.kts.ticketapp.service.sector.get.GetSectorIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GetSectorIntegrationTest.class,
        DeleteSectorIntegrationTest.class
})
public class SectorServiceIntegrationSuite {
}
