package nvt.kts.ticketapp.service.common.email.ticket;

import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.service.common.email.EmailClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoldTicketsReminderService {

    private static final String EMAIL_SUBJECT = "Reminder about upcoming events";

    private EmailClient emailClient;

    public SoldTicketsReminderService(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    public void sendReminderForSoldTicket(String emailTo, Ticket ticket) {

        String content = generateContent(ticket);

        emailClient.sendMimeEmail(
                emailTo,
                EMAIL_SUBJECT,
                content
        );
    }

    private String generateContent(Ticket ticket) {

        String msg = "";
        msg += "<html><body>";

        msg += "<p> We are reminding you about an upcoming event:";
        msg += "<div>";
        msg += "Event: " + ticket.getEventDay().getEvent().getName()  + "<br/>";
        msg += "Location: " + ticket.getEventDay().getLocation().getScheme().getName()+ "<br/>";
        msg += "Date: " +  ticket.getEventDay().getDate() + "</p>";

        if (ticket.getSeatCol() != 0 && ticket.getSeatRow() != 0) {
            msg += "<p> Your seat: <br/>";
            msg += "Row:" + ticket.getSeatRow() + "<br/>";
            msg += "Column: " +  ticket.getSeatCol() + "</p>";
        }
        msg+= "</div><br/>";
        msg += "</body></html>";

        return msg;
    }
}
