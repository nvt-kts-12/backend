package nvt.kts.ticketapp.exception.ticket;

public class TicketDoesNotExist extends Exception {
    public TicketDoesNotExist(Long id) {
        super("Ticket with id" + id + "does not exist");
    }
}
