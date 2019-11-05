package nvt.kts.ticketapp.exception.event;

public class EventdayNotFound extends Exception {
    public  EventdayNotFound(Long id) {
        super("Event day with id: " + id + " does not exist");
    }

}
