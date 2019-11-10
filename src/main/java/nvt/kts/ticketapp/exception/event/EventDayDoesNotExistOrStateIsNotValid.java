package nvt.kts.ticketapp.exception.event;

public class EventDayDoesNotExistOrStateIsNotValid extends Exception {

    public EventDayDoesNotExistOrStateIsNotValid(Long id) {
        super(String.format("Event day with id %s doesn't exist or state is invalid", id));
    }
}
