package nvt.kts.ticketapp.util;

import nvt.kts.ticketapp.exception.date.DateFormatIsNotValid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static nvt.kts.ticketapp.config.Constants.DATE_TIME_FORMAT;

public class DateUtil {

    public static Date parseDate(String date, String format) throws DateFormatIsNotValid {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new DateFormatIsNotValid();
        }
    }



    public static boolean datesEqual(Date date1, Date date2) throws ParseException {

        Date date1formatted = setTimeToMidnight(date1);
        Date date2formatted = setTimeToMidnight(date2);

        return date1formatted.compareTo(date2formatted) == 0;
    }

    public static Date setTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime( date );
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static boolean dateInPast(Date date) {

        Date today = setTimeToMidnight(new Date());
        Date dateFormatted = setTimeToMidnight(date);

        return dateFormatted.before(today);

    }
}
