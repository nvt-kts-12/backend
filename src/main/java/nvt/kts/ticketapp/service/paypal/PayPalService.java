package nvt.kts.ticketapp.service.paypal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import nvt.kts.ticketapp.domain.dto.paypal.PayPalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayPalService {

    @Autowired
    private APIContext apiContext;


    public Map<String, Object> createPayment(String sum) {
        Map<String, Object> response = new HashMap<String, Object>();
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(sum);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Tickets purchase on nvt-kts application");
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:4200/");
        redirectUrls.setReturnUrl("http://localhost:4200/pay-pal");
        payment.setRedirectUrls(redirectUrls);
        Payment createdPayment;
        try {
            String redirectUrl = "";
            createdPayment = payment.create(apiContext);
            if (createdPayment != null) {
                List<Links> links = createdPayment.getLinks();
                for (Links link : links) {
                    if (link.getRel().equals("approval_url")) {
                        redirectUrl = link.getHref();
                        break;
                    }
                }
                response.put("status", "success");
                response.put("redirect_url", redirectUrl);
            }
        } catch (PayPalRESTException e) {
            System.out.println("Error happened during payment creation!");
        }
        return response;
    }

    public PayPalDto completePayment(HttpServletRequest req) {
        PayPalDto payPalDto = new PayPalDto("fail");
        Payment payment = new Payment();
        payment.setId(req.getParameter("paymentId"));

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(req.getParameter("payerId"));


        try {
            Payment createdPayment = payment.execute(apiContext, paymentExecution);
            payPalDto.setStatus("success");
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
        return payPalDto;
    }
}
