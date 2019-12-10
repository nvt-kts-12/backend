package nvt.kts.ticketapp.service.common.email;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import static nvt.kts.ticketapp.config.Constants.QR_CODE_PATH;

@Service
public class EmailClient {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public void sendMimeEmail(String emailTo, String subject, String content, List<Ticket> tickets) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

            messageHelper.setText(content, true);

            for (Ticket ticket: tickets) {
                FileSystemResource file = new FileSystemResource(new File(QR_CODE_PATH + String.valueOf(ticket.getId()) + ".png"));
                messageHelper.addInline("qr_code_" + String.valueOf(ticket.getId()), file);
            }
            messageHelper.setFrom(emailFrom);
            messageHelper.setTo("isamrs19mail@gmail.com");
            messageHelper.setSubject(subject);

        };

        try {
            javaMailSender.send(messagePreparator);
            for (Ticket ticket: tickets) {
                File fileToDelete = new File(QR_CODE_PATH + String.valueOf(ticket.getId()) + ".png");
                fileToDelete.delete();
            }
        } catch (MailException e) {
            // runtime exception; compiler will not force you to handle it
        }
    }


    public void generateQrCode(String ticketId) throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode("Ticket id is: " + ticketId, BarcodeFormat.QR_CODE, 15, 15);

        Path path = FileSystems.getDefault().getPath(QR_CODE_PATH + ticketId + ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

    }
}
