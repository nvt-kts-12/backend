package nvt.kts.ticketapp.service.ticket;


import com.google.zxing.WriterException;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayDTO;
import nvt.kts.ticketapp.domain.dto.event.EventEventDaysDTO;
import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.date.DateCantBeInThePast;
import nvt.kts.ticketapp.exception.date.DateFormatIsNotValid;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExist;
import nvt.kts.ticketapp.exception.event.EventDaysListEmpty;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.exception.event.ReservationExpireDateInvalid;
import nvt.kts.ticketapp.exception.location.LocationNotAvailableThatDate;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.exception.sector.SectorCapacityOverload;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.exception.ticket.ReservationCanNotBeCancelled;
import nvt.kts.ticketapp.exception.ticket.TicketDoesNotExist;
import nvt.kts.ticketapp.exception.ticket.TicketNotFoundOrAlreadyBought;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.service.event.EventDayServiceImpl;
import nvt.kts.ticketapp.service.event.EventServiceImpl;
import nvt.kts.ticketapp.service.ticket.TicketServiceImpl;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketServiceJUnitTest {

    @MockBean
    EventRepository eventRepository;
    @MockBean
    TicketRepository ticketRepository;
    @MockBean
    EventDaysRepository eventDaysRepository;

    @Autowired
    EventServiceImpl eventService;
    @Autowired
    TicketServiceImpl ticketService;
    @Autowired
    EventDayServiceImpl eventDayService;

    @Before
    public void setup() {
        Event e3 = new Event("name3", EventCategory.ENTERTAINMENT, "good3");
        //tickets, eventdays
        LocationScheme scheme = new LocationScheme("petrovaradin", "Novi Sad");
        Location location = new Location(scheme);
        EventDay eventDay = new EventDay(new Date(2020 - 01 - 15), location, new Date(2020 - 02 - 15), EventDayState.SOLD_OUT, e3);
        eventDay.setId(1L);

        Ticket t1 = new Ticket(false, 1L, 1, 1, 123, new EventDay(), new User(), true);
        t1.setDeleted(false);

        List<Ticket> tickets = new ArrayList<Ticket>();
        tickets.add(t1);

        Mockito.when(eventDaysRepository.findOneByIdAndStateAndDateBefore(eventDay.getId(), EventDayState.RESERVABLE_AND_BUYABLE, eventDay.getDate())).thenReturn(Optional.of(eventDay));
        Mockito.when(eventDaysRepository.findById(eventDay.getId())).thenReturn(Optional.of(eventDay));
        Mockito.when(ticketService.saveAll(tickets)).thenReturn(tickets);

    }


    @Test
    public void soldOutTest() throws EventDayDoesNotExist {

        EventDay eventDay = eventDayService.findOneById(1L);
        ticketService.getAvailableTickets(eventDay.getId());
        eventService.checkIfEventDayIsSoldOut(eventDay);
        assertNotNull(eventDay);
        assertThat(eventDay.getState() == EventDayState.SOLD_OUT);

    }

    @Test
    public void saveAllTest(){
        Ticket t1 = new Ticket(false, 1L, 1, 1, 123, new EventDay(), new User(), true);
        List<Ticket> tickets = new ArrayList<Ticket>();
        tickets.add(t1);

        List<Ticket> returnedTickets =ticketService.saveAll(tickets);
        assertNotNull(returnedTickets);
    }

    @Test(expected = TicketNotFoundOrAlreadyBought.class)
    public void buyTicket_TicketNotFoundOrAlreadyBought() throws WriterException, IOException, TicketNotFoundOrAlreadyBought {
        Long ticketId= 1L;
        Mockito.when(ticketRepository.findOneByIdAndSoldFalse(ticketId)).thenReturn(Optional.empty());

        ticketService.buyTicket(ticketId);

    }

    @Test()
    public void buyTicket() throws WriterException, IOException, TicketNotFoundOrAlreadyBought {
        //@NotNull boolean sold, @NotNull Long sectorId, @NotNull double price, boolean vip, EventDay eventDay, User user
        boolean sold = false;
        Long sectorId = 1L;
        double price = 100;
        boolean vip = false;
        Long ticketId= 1L;

        LocationScheme lc = new LocationScheme("scheme","novisad");
        Location location = new Location(lc);
        Event e1 = new Event("name1", EventCategory.ENTERTAINMENT, "good2");
        EventDay eventDay1 = new EventDay(new Date(2-1-2019),location,new Date(3-1-2019), EventDayState.RESERVABLE_AND_BUYABLE,e1);
        User user = new User("username","pass","name","lastname","email@gmail.com");

        Ticket ticket = new Ticket(false,sectorId,price,vip,eventDay1,user);

        Mockito.when(ticketRepository.findOneByIdAndSoldFalse(ticketId)).thenReturn(Optional.of(ticket));

        Ticket success = ticketService.buyTicket(ticketId);
        assertThat(HttpStatus.OK);

    }

    @Test(expected = TicketDoesNotExist.class)
    public void cancelReservation_TicketDoesNotExist() throws TicketDoesNotExist, ReservationCanNotBeCancelled {
        Long ticketId=1L;
        Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());
        TicketDTO cancelTicket = ticketService.cancelReservation(ticketId);
    }

    @Test
    public void cancelReservation_valid() throws TicketDoesNotExist, ReservationCanNotBeCancelled {
        boolean sold = false;
        Long sectorId = 1L;
        double price = 100;
        boolean vip = false;

        LocationScheme lc = new LocationScheme("scheme","novisad");
        Location location = new Location(lc);
        Event e1 = new Event("name1", EventCategory.ENTERTAINMENT, "good2");
        EventDay eventDay1 = new EventDay(new Date(2-1-2019),location,new Date(3-1-2019), EventDayState.RESERVABLE_AND_BUYABLE,e1);
        User user = new User("username","pass","name","lastname","email@gmail.com");
        user.setId(5L);

        Ticket ticket = new Ticket(sold,sectorId,price,vip,eventDay1,user);

        Mockito.when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        TicketDTO cancelTicket = ticketService.cancelReservation(ticket.getId());
        assertNotNull(cancelTicket);
        assertThat(cancelTicket.getEventDayState().equals(EventDayState.RESERVABLE_AND_BUYABLE));
        assertThat(cancelTicket.getUser() == null);

    }

    @Test(expected = ReservationCanNotBeCancelled.class)
    public void cancelReservation_ExpectedReservationCannotBeCancelled() throws TicketDoesNotExist, ReservationCanNotBeCancelled {
        boolean sold = false;
        Long sectorId = 1L;
        double price = 100;
        boolean vip = false;
        Long ticketId= 1L;

        LocationScheme lc = new LocationScheme("scheme","novisad");
        Location location = new Location(lc);
        Event e1 = new Event("name1", EventCategory.ENTERTAINMENT, "good2");
        EventDay eventDay1 = new EventDay(new Date(2-1-2019),location,new Date(3-1-2019), EventDayState.RESERVABLE_AND_BUYABLE,e1);
        User user = new User("username","pass","name","lastname","email@gmail.com");

        Ticket ticket = new Ticket(true,sectorId,price,vip,eventDay1,user);

        Mockito.when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        TicketDTO cancelTicket = ticketService.cancelReservation(ticket.getId());
        assertNotNull(cancelTicket);
        assertThat(cancelTicket.getEventDayState().equals(EventDayState.RESERVABLE_AND_BUYABLE));
        assertThat(cancelTicket.getUser() == null);

    }




}