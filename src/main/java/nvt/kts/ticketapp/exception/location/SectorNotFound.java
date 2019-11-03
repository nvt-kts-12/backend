package nvt.kts.ticketapp.exception.location;

public class SectorNotFound extends Exception {
    public SectorNotFound(Long id) {
        super("Sector with id: " + id + " does not exist or is deleted.");
    }
}
