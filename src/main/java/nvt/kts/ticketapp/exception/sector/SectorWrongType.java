package nvt.kts.ticketapp.exception.sector;

public class SectorWrongType extends Exception {

    public SectorWrongType(Long id) {
        super(String.format("Sector with id %s is wrong type", id));
    }
}
