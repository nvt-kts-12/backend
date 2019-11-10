package nvt.kts.ticketapp.exception.location;

public class LocationNotAvailableThatDate extends Exception {

    public LocationNotAvailableThatDate() {
        super("Location is not available for that date");
    }
}
