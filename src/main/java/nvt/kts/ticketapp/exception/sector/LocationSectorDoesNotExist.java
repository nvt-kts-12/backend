package nvt.kts.ticketapp.exception.sector;

public class LocationSectorDoesNotExist extends Exception{
    public LocationSectorDoesNotExist(Long sectorId){
        super("Location scheme sector with id: " + sectorId + " does not exist.");
    }
}
