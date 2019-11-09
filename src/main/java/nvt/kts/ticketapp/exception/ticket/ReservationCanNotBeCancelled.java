package nvt.kts.ticketapp.exception.ticket;

public class ReservationCanNotBeCancelled extends Exception {
    public ReservationCanNotBeCancelled(){
        super("This reservation can not be cancelled!");
    }
}
