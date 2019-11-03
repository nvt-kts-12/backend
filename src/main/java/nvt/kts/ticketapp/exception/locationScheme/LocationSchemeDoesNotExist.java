package nvt.kts.ticketapp.exception.locationScheme;

public class LocationSchemeDoesNotExist extends Exception {

    public LocationSchemeDoesNotExist(Long id) {
        super("Location scheme with id" + id + " doesn't exist");
    }
}
