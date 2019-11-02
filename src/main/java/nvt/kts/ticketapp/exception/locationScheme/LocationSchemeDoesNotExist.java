package nvt.kts.ticketapp.exception.locationScheme;

public class LocationSchemeDoesNotExist extends Exception {

    public LocationSchemeDoesNotExist() {
        super("Location scheme doesn't exist");
    }
}
