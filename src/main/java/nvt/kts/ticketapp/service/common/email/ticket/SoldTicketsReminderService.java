package nvt.kts.ticketapp.service.common.email.ticket;

import com.google.zxing.WriterException;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.service.common.email.EmailClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class SoldTicketsReminderService {

    private static final String EMAIL_SUBJECT = "Reminder about upcoming events";

    private EmailClient emailClient;

    public SoldTicketsReminderService(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    public void sendReminderForSoldTicket(String emailTo, Ticket ticket) throws IOException, WriterException {

        String content = generateContent(ticket);

        emailClient.sendMimeEmail(
                emailTo,
                EMAIL_SUBJECT,
                content,
                Arrays.asList(ticket)
        );
    }

    private String generateContent(Ticket ticket) throws IOException, WriterException {

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
        emailClient.generateQrCode(String.valueOf(ticket.getId()));
        msg += "<img src='cid:qr_code_" + String.valueOf(ticket.getId()) + "' width='500px' height='500px'>";
        msg+= "</div><br/>";
        msg += "</body></html>";

        return msg;
    }
}
