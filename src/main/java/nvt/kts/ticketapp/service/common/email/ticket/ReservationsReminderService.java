package nvt.kts.ticketapp.service.common.email.ticket;

import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.service.common.email.EmailClient;
import org.springframework.stereotype.Service;

@Service
public class ReservationsReminderService {

    private static final String EMAIL_SUBJECT = "Reminder about expiring reservations";

    private EmailClient emailClient;

    public ReservationsReminderService(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    public void sendReminderForExpiringReservation(String emailTo, Ticket ticket) {

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

        msg += "<p> We are reminding you that your reservation is about to expire:";

        msg += "<div>";
        msg += "<p> Ticket id: " + ticket.getId() + "<br/>";
        msg += "Event: " + ticket.getEventDay().getEvent().getName()  + "<br/>";
        msg += "Location: " + ticket.getEventDay().getLocation().getScheme().getName()+ "<br/>";
        msg += "Date: " +  ticket.getEventDay().getDate() + "<br/>";
        msg += "Price: " + ticket.getPrice()  + "<br/>";
        msg += "Type of ticket: " + (ticket.isVip() ? "VIP" : "STANDARD") + "<br/>";
        msg += "Sector:" + ticket.getSectorId() + "<br/>";

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
