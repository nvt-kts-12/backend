package nvt.kts.ticketapp.exception.location;

public class LocationSchemeNotFound extends Exception {
    public LocationSchemeNotFound(Long id) {
        super("Location scheme with id: " + id + " does not exist or is deleted.");
    }
}
