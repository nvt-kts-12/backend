package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.service.location.delete.DeleteLocationSchemeUnitTest;
import nvt.kts.ticketapp.service.location.get.GetLocationSchemeUnitTest;
import nvt.kts.ticketapp.service.location.save.SaveLocationSchemeUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SaveLocationSchemeUnitTest.class,
        GetLocationSchemeUnitTest.class,
        DeleteLocationSchemeUnitTest.class
})
public class LocationSchemeServiceUnitSuite {

}