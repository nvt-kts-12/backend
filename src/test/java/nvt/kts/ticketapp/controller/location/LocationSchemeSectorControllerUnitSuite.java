package nvt.kts.ticketapp.controller.location;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        DeleteLocationSchemeSectorControllerUnitTest.class,
        GetLocationSchemeSectorControllerUnitTest.class,
        SaveLocationSchemeSectorControllerUnitTest.class,
        LocationSchemeControllerUnitTest.class,
        SectorControllerUnitTest.class
})
public class LocationSchemeSectorControllerUnitSuite {
}