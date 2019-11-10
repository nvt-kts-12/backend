package nvt.kts.ticketapp.service.common.email.ticket;

import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.service.common.email.EmailClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketEmailService  {

    private static final String EMAIL_SUBJECT = "Purchased tickets";

    private EmailClient emailClient;

    public TicketEmailService(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    public void sendEmailForPurchasedTickets(String emailTo, List<Ticket> tickets) {

        String content = generateContent(tickets);

        emailClient.sendMimeEmail(
                emailTo,
                EMAIL_SUBJECT,
                content
        );
    }

    private String generateContent(List<Ticket> tickets) {

        String msg = "";
        msg += "<html><body>";

        msg += "<p> You have successfully purched " + ((tickets.size() == 1) ? "ticket." : "tickets.");
        for(Ticket ticket : tickets) {
            msg += "<div>";
            msg += "<p> Ticket id: " + ticket.getId() + "</p>";
            msg += "<p> Event: " + ticket.getEventDay().getEvent().getName()  + "</p>";
            msg += "<p> Location: " + ticket.getEventDay().getLocation().getScheme().getName()+ "</p>";
            msg += "<p> Date: " +  ticket.getEventDay().getDate() + "</p>";
            msg += "<p> Price: " + ticket.getPrice()  + "</p>";
            msg += "<p> Type of ticket: " + (ticket.isVip() ? "VIP" : "STANDARD") + "</p>";
            msg += "<p> Sector:" + ticket.getSectorId() + "</p>";

            if (ticket.getSeatCol() == 0 && ticket.getSeatRow() != 0) {
                msg += "<p> Row:" + ticket.getSeatRow() + "</p>";
                msg += "<p> Column: " +  ticket.getSeatCol() + "</p>";
            }
            msg+= "</div><br>";
        }
        msg += "</body></html>";

        return msg;
    }
}
