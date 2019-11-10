package nvt.kts.ticketapp.exception.user;

public class EmailAlreadyExist extends Exception {
    public EmailAlreadyExist() {
        super("Email already exist");
    }
}
