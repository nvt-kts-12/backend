package nvt.kts.ticketapp.controller.paypal;

import lombok.RequiredArgsConstructor;
import nvt.kts.ticketapp.domain.dto.paypal.PayPalDto;
import nvt.kts.ticketapp.service.paypal.PayPalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/paypal")
public class PayPalController {

    public static final String STATUS = "status";
    public static final String SUCCESS = "success";
    private final PayPalService payPalService;

    @PostMapping(value = "/complete/payment")
    public ResponseEntity completePayment(HttpServletRequest request,
                                          @RequestParam("paymentId") String paymentId,
                                          @RequestParam("payerId") String payerId) {
        return new ResponseEntity<PayPalDto>(payPalService.completePayment(request), HttpStatus.OK);
    }
}