package nvt.kts.ticketapp.exception.user;

public class UsernameAlreadyExist extends Exception{

    public UsernameAlreadyExist() {
        super("Username already exist");
    }
}
