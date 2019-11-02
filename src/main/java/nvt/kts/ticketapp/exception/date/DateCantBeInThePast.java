package nvt.kts.ticketapp.exception.date;

public class DateCantBeInThePast extends Exception {

    public DateCantBeInThePast() {
        super("Date can't be in the past");
    }
}
