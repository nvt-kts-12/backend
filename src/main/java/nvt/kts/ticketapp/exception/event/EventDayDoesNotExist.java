package nvt.kts.ticketapp.exception.event;

public class EventDayDoesNotExist extends Exception {

    public EventDayDoesNotExist() {
        super("Event day doesn't exist");
    }
}
