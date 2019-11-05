package nvt.kts.ticketapp.exception.ticket;

public class TicketNotFoundOrAlreadyBought extends Exception {
    public TicketNotFoundOrAlreadyBought(Long id) {
        super(String.format("Ticket with id %s not found or it is already bought.", id));
    }
}
