package nvt.kts.ticketapp.exception.ticket;

public class NumberOfTicketsException extends  Exception {

    public NumberOfTicketsException() {
        super("We don't have that number of tickets available");
    }
}
