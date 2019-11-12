package nvt.kts.ticketapp.service.common.email.ticket;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.service.common.email.EmailClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import static nvt.kts.ticketapp.config.Constants.QR_CODE_PATH;

@Service
public class TicketEmailService  {

    private static final String EMAIL_SUBJECT = "Purchased tickets";

    private EmailClient emailClient;

    public TicketEmailService(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    public void sendEmailForPurchasedTickets(String emailTo, List<Ticket> tickets) throws IOException, WriterException {

        generateQrCode(tickets.get(0).getUser().getUsername());
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
        msg += "<p> Scan this QR code to acces your tickets on our site:</p> <br>";
        msg += "<img src='cid:qr_code' width='500px' height='500px'>";
        msg += "</body></html>";

        return msg;
    }

    private void generateQrCode(String username) throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(username, BarcodeFormat.QR_CODE, 15, 15);

        Path path = FileSystems.getDefault().getPath(QR_CODE_PATH);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

    }
}
