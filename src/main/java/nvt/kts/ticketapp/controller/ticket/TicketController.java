package nvt.kts.ticketapp.controller.ticket;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.exception.ticket.ReservationCanNotBeCancelled;
import nvt.kts.ticketapp.exception.ticket.TicketDoesNotExist;
import nvt.kts.ticketapp.exception.ticket.TicketListCantBeEmpty;
import nvt.kts.ticketapp.exception.ticket.TicketNotFoundOrAlreadyBought;
import nvt.kts.ticketapp.service.event.EventService;
import nvt.kts.ticketapp.service.paypal.PayPalService;
import nvt.kts.ticketapp.service.ticket.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final PayPalService payPalService;


    @PutMapping("/buy/{id}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity buyTicket(@PathVariable Long id) {
        try {
            Ticket ticket = ticketService.buyTicket(id);

            Map<String, Object> createdPayment = payPalService.createPayment(String.valueOf(ticket.getPrice()));
            return new ResponseEntity<>(createdPayment, HttpStatus.OK);
        } catch (TicketNotFoundOrAlreadyBought ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException | WriterException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Could not generate QR code", HttpStatus.EXPECTATION_FAILED);
        } catch (TicketListCantBeEmpty ticketListCantBeEmpty) {
            ticketListCantBeEmpty.printStackTrace();
            return new ResponseEntity<String>("Something went wrong! Please try again.", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity cancelReservation(@PathVariable Long id) {
        try {
            return new ResponseEntity<TicketDTO>(ticketService.cancelReservation(id), HttpStatus.OK);
        } catch (TicketDoesNotExist | ReservationCanNotBeCancelled ticketDoesNotExist) {
            ticketDoesNotExist.printStackTrace();
            return new ResponseEntity<String>(ticketDoesNotExist.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{sectorId}/{eventDayId}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity getBySectorAndDay(@PathVariable Long sectorId, @PathVariable Long eventDayId) {
        return new ResponseEntity<List<TicketDTO>>(ticketService.getAllTicketsForSectorAndEventDay(sectorId, eventDayId), HttpStatus.OK);
    }
}