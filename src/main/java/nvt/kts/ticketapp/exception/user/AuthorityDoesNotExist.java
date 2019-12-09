package nvt.kts.ticketapp.exception.user;

public class AuthorityDoesNotExist extends Exception {
    public AuthorityDoesNotExist(String s) {
        super("Authority " + s + " doesn't exist");
    }
}
