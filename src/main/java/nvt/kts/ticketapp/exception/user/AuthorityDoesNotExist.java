package nvt.kts.ticketapp.exception.user;

public class AuthorityDoesNotExist extends Exception {
    public AuthorityDoesNotExist(long l) {
        super("Authority with id " + l + " doesn't exist");
    }
}
