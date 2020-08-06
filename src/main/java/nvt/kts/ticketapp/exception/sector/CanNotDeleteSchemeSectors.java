package nvt.kts.ticketapp.exception.sector;

public class CanNotDeleteSchemeSectors extends Exception {
    public CanNotDeleteSchemeSectors(Long schemeId){
        super("Location scheme with id " + schemeId + " can not be deleted because one of its sectors is used in some event.");
    }
}
