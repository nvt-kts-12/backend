package nvt.kts.ticketapp.exception.sector;

public class CanNotDeleteSchemeSectors extends Exception {
    public CanNotDeleteSchemeSectors(Long sectorId){
        super("Location scheme sector with id: " + sectorId + " can not be deleted because it is used in some event.");
    }
}
