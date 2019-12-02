package nvt.kts.ticketapp.exception.sector;

public class CanNotDeleteSchemeSectors extends Exception {
    public CanNotDeleteSchemeSectors(){
        super("Location scheme can not be deleted because it is used in some event.");
    }
}
