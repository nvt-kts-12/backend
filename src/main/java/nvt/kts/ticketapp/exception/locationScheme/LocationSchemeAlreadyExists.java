package nvt.kts.ticketapp.exception.locationScheme;

public class LocationSchemeAlreadyExists extends Exception {
    public LocationSchemeAlreadyExists(String name) {
        super("Location scheme with name " + name + " already exists.");
    }
}
