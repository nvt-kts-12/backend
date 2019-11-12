package nvt.kts.ticketapp.config;

public class Constants {

    public static final String EMAIL_REGEX = "([a-zA-Z0-9]+(?:[. _+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})";

    // minimum 8 characters, at least one uppercase letter, one lowercase letter, one number and one special character
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    // minimum 3 characters, letters and numbers, first must be letter
    public static final String USERNAME_REGEX = "[a-zA-Z]{1}[a-zA-Z0-9]{2,}";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String WHITESPACES_REGEX = " *";

    public static final String QR_CODE_PATH = "src/main/resources/qr_code.png";
}
