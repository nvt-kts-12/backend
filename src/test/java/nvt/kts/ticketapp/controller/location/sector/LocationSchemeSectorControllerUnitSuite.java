package nvt.kts.ticketapp.controller.location.sector;

import nvt.kts.ticketapp.controller.location.LocationSchemeControllerUnitTest;
import nvt.kts.ticketapp.controller.location.sector.delete.DeleteLocationSchemeSectorControllerUnitTest;
import nvt.kts.ticketapp.controller.location.sector.get.GetLocationSchemeSectorControllerUnitTest;
import nvt.kts.ticketapp.controller.location.sector.save.SaveLocationSchemeSectorControllerUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        DeleteLocationSchemeSectorControllerUnitTest.class,
        GetLocationSchemeSectorControllerUnitTest.class,
        SaveLocationSchemeSectorControllerUnitTest.class,
        LocationSchemeControllerUnitTest.class,
        SectorControllerUnitTest.class,
        SectorControllerIntegrationTest.class
})
public class LocationSchemeSectorControllerUnitSuite {
}