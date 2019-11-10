package nvt.kts.ticketapp.exception.location;

public class LocationSectorsDoesNotExistForLocation extends Exception {

    public LocationSectorsDoesNotExistForLocation(Long id) {
        super(String.format("Location sector with %s id doesn't exist", id));
    }
}
