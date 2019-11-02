package nvt.kts.ticketapp.exception.date;

public class DateCantBeInPast extends Exception {

    public DateCantBeInPast() {
        super("Date can't be in past");
    }
}
