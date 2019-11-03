package nvt.kts.ticketapp.exception.location;

public class LocationSchemeAlreadyExists extends Exception {
    public LocationSchemeAlreadyExists(String name) {
        super("Location scheme with name " + name + " already exists.");
    }
}
