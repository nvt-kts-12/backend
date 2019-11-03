package nvt.kts.ticketapp.exception.locationScheme;

public class CanNotDeleteScheme extends Exception {
    public CanNotDeleteScheme(Long id){
        super("Location scheme with id: " + id + " can't be deleted because there is at least one Location with" +
                " this location scheme.");
    }
}
