package nvt.kts.ticketapp.service.reminder;

import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.service.common.email.ticket.ReservationsReminderService;
import nvt.kts.ticketapp.service.common.email.ticket.SoldTicketsReminderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ReminderServiceImpl implements ReminderService {

    private final int DAYS_DIFFERENCE = 2;

    private final TicketRepository ticketRepository;
    private final SoldTicketsReminderService soldTicketsReminderService;
    private final ReservationsReminderService reservationsReminderService;

    public ReminderServiceImpl(TicketRepository ticketRepository,
                               SoldTicketsReminderService soldTicketsReminderService,
                               ReservationsReminderService reservationsReminderService) {
        this.ticketRepository = ticketRepository;
        this.soldTicketsReminderService = soldTicketsReminderService;
        this.reservationsReminderService = reservationsReminderService;
    }

    @Override
    public boolean sendReminders() {
        boolean reservationsRemindersSent = checkReservations();
        boolean soldTicketsRemindersSent = checkSoldTickets();

        return reservationsRemindersSent || soldTicketsRemindersSent;
    }

    private boolean checkSoldTickets() {

        boolean remindersSent = false;

        List<Ticket> soldTickets = ticketRepository.findAllBySoldTrueAndUserNotNull();

        Date today = new Date();

        for (Ticket t : soldTickets) {

            Date eventDate = t.getEventDay().getDate();

            long diffInMillies = Math.abs(eventDate.getTime() - today.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diff == DAYS_DIFFERENCE) {
                soldTicketsReminderService.sendReminderForSoldTicket(t.getUser().getEmail(), t);
                remindersSent = true;
            }
        }

        return remindersSent;
    }

    private boolean checkReservations() {

        boolean remindersSent = false;

        List<Ticket> reservations = ticketRepository.findAllBySoldFalseAndUserNotNull();
        Date today = new Date();

        for (Ticket t : reservations) {
            Date expirationDate = t.getEventDay().getReservationExpirationDate();

            long diffInMillies = Math.abs(expirationDate.getTime() - today.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diff == DAYS_DIFFERENCE) {
                reservationsReminderService.sendReminderForExpiringReservation(t.getUser().getEmail(), t);
                remindersSent = true;
            }
        }

        return remindersSent;
    }
}
