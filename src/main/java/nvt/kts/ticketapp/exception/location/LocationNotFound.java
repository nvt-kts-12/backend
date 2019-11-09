package nvt.kts.ticketapp.exception.location;

public class LocationNotFound extends Exception {
    public LocationNotFound(Long id){
        super("Location with id: " + id + " does not exist.");
    }
}
