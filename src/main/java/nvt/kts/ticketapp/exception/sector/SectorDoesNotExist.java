package nvt.kts.ticketapp.exception.sector;

public class SectorDoesNotExist extends  Exception {

    public SectorDoesNotExist() {
        super("Sector doesn't exist");
    }
}
