package nvt.kts.ticketapp.exception.event;

public class EventNotFound extends Exception {

    public EventNotFound(Long id){
        super("Event with id: " + id + " does not exist");
    }
}
