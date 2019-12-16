package nvt.kts.ticketapp.controller.location.sector;

import nvt.kts.ticketapp.controller.location.sector.delete.DeleteLocationSchemeSectorControllerIntegrationTest;
import nvt.kts.ticketapp.controller.location.sector.get.GetLocationSchemeSectorControllerIntegrationTest;
import nvt.kts.ticketapp.controller.location.sector.save.SaveLocationSchemeSectorIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DeleteLocationSchemeSectorControllerIntegrationTest.class,
        GetLocationSchemeSectorControllerIntegrationTest.class,
        SaveLocationSchemeSectorIntegrationTest.class
})
public class LocationSchemeSectorControllerIntegrationSuite {
}
