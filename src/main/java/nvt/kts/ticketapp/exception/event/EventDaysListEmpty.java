package nvt.kts.ticketapp.exception.event;

public class EventDaysListEmpty extends Exception {

    public  EventDaysListEmpty() {
        super("Event days list can't be empty");
    }
}
