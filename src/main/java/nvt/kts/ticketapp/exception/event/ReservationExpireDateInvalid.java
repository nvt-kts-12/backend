package nvt.kts.ticketapp.exception.event;

public class ReservationExpireDateInvalid extends Exception {

    public ReservationExpireDateInvalid() {
        super("Reservation expire date must be before event date");
    }
}
