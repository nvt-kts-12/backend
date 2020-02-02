package nvt.kts.ticketapp.exception.event;

public class EventDayForDateExists extends Exception {

    public EventDayForDateExists() {
        super("An eventday for this date already exists.");
    }
}
