package nvt.kts.ticketapp.exception.sector;

public class CanNotDeleteSchemeSectors extends Exception {
    public CanNotDeleteSchemeSectors(Long schemeId){
        super("Location scheme with id: " + schemeId + "can not be deleted because it is used in some event.");
    }
}
