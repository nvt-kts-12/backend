package nvt.kts.ticketapp.service.event.reserve;

import com.google.zxing.WriterException;
import nvt.kts.ticketapp.domain.dto.event.EventDayReservationDTO;
import nvt.kts.ticketapp.domain.dto.event.ParterDTO;
import nvt.kts.ticketapp.domain.dto.event.SeatDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExistOrStateIsNotValid;
import nvt.kts.ticketapp.exception.event.EventdayNotFound;
import nvt.kts.ticketapp.exception.location.LocationSectorsDoesNotExistForLocation;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import nvt.kts.ticketapp.exception.sector.SectorWrongType;
import nvt.kts.ticketapp.exception.ticket.NumberOfTicketsException;
import nvt.kts.ticketapp.exception.ticket.ReservationIsNotPossible;
import nvt.kts.ticketapp.exception.ticket.SeatIsNotAvailable;
import nvt.kts.ticketapp.exception.ticket.TicketListCantBeEmpty;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.service.event.EventDayService;
import nvt.kts.ticketapp.service.event.EventService;
import nvt.kts.ticketapp.service.sector.LocationSectorService;
import nvt.kts.ticketapp.service.ticket.TicketService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReserveEventUnitTest {

    @MockBean
    private EventDayService eventDayServiceMocked;

    @MockBean
    private LocationSectorService locationSectorServiceMocked;

    @MockBean
    private TicketRepository ticketRepositoryMocked;

    @MockBean
    private TicketService ticketServiceMocked;

    @Autowired
    private EventService eventService;


    private User generateUser() {
        User user = new User("username", "password", "firstname", "lastname", "email@gmail.com");
        user.setId(1L);

        return user;
    }

    private Location generateLocation() {
        LocationScheme locationScheme = generateLocationScheme();
        Location location = new Location(locationScheme);
        location.setId(1L);

        return location;
    }

    private LocationScheme generateLocationScheme() {
        LocationScheme locationScheme = new LocationScheme("Scheme", "Address");
        locationScheme.setId(1L);

        return locationScheme;
    }

    private EventDay generateEventDay() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        Date date = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, -2);
        Date reservationExpirationDate = calendar.getTime();

        Event event = new Event("Event", EventCategory.ENTERTAINMENT, "desc");
        event.setId(1L);

        Location location = generateLocation();

        EventDay eventDay = new EventDay(date, location, reservationExpirationDate, EventDayState.RESERVABLE_AND_BUYABLE, event);
        eventDay.setId(1L);

        return eventDay;
    }

    private Sector generateSector_Grandstand() {

        LocationScheme locationScheme = generateLocationScheme();

        Sector sector2 = new Sector(0.0, 0.0, 0.0, 0.0, 2, 2, 1, SectorType.GRANDSTAND, locationScheme);
        sector2.setId(2L);

        return sector2;
    }

    private Sector generateSector_Parter() {

        LocationScheme locationScheme = generateLocationScheme();

        Sector sector1 = new Sector(0.0, 0.0, 0.0, 0.0, 2, 2, 1, SectorType.PARTER, locationScheme);
        sector1.setId(1L);

        return sector1;
    }

    private LocationSector generateLocationSector_Parter() {
        Sector sectorParter = generateSector_Parter();
        Location location = generateLocation();

        LocationSector locationSector1 = new LocationSector(sectorParter, location, 500, 2, false);
        locationSector1.setId(1L);

        return locationSector1;
    }

    private LocationSector generateLocationSector_Grandstand() {

        Sector sectorGrandstand = generateSector_Grandstand();
        Location location = generateLocation();

        LocationSector locationSector2 = new LocationSector(sectorGrandstand, location, 500, 2, false);
        locationSector2.setId(2L);

        return locationSector2;
    }

    private Ticket generateTicket_Parter() {
        EventDay eventDay = generateEventDay();

        boolean sold = false;
        Long sectorId = 1L;
        int seatRow = 0;
        int seatCol = 0;
        double price = 200;
        User user = null;
        boolean vip = false;
        Ticket ticket = new Ticket(sold, sectorId, seatRow, seatCol, price, eventDay, user, vip);
        ticket.setId(1L);
        return ticket;
    }

    private Ticket generateTicket_Grandstand() {
        EventDay eventDay = generateEventDay();

        boolean sold = false;
        Long sectorId = 2L;
        int seatRow = 1;
        int seatCol = 1;
        double price = 200;
        User user = null;
        boolean vip = false;
        Ticket ticket = new Ticket(sold, sectorId, seatRow, seatCol, price, eventDay, user, vip);
        ticket.setId(2L);
        return ticket;
    }

    private EventDayReservationDTO generateEventDayReservationDTO_Parter() {
        EventDayReservationDTO eventDayReservationDTO_Parter = new EventDayReservationDTO();
        eventDayReservationDTO_Parter.setEventDayId(1L);
        eventDayReservationDTO_Parter.setPurchase(false);
        ParterDTO parterDTO = new ParterDTO();
        parterDTO.setNumberOfTickets(1);
        parterDTO.setSectorId(1L);
        eventDayReservationDTO_Parter.setParters(new ArrayList<>(Collections.singletonList(parterDTO)));
        eventDayReservationDTO_Parter.setSeats(new ArrayList<>());

        return eventDayReservationDTO_Parter;
    }

    private EventDayReservationDTO generateEventDayReservationDTO_Grandstand() {
        EventDayReservationDTO eventDayReservationDTO_Grandstand = new EventDayReservationDTO();
        eventDayReservationDTO_Grandstand.setEventDayId(1L);
        eventDayReservationDTO_Grandstand.setPurchase(false);
        SeatDTO seatDTO = new SeatDTO();
        seatDTO.setCol(1);
        seatDTO.setRow(1);
        seatDTO.setSectorId(2L);
        eventDayReservationDTO_Grandstand.setParters(new ArrayList<>());
        eventDayReservationDTO_Grandstand.setSeats(new ArrayList<>(Collections.singletonList(seatDTO)));

        return eventDayReservationDTO_Grandstand;
    }

    private EventDayReservationDTO generateEventDayReservationDTO_Parter_and_Grandstand() {

        EventDayReservationDTO eventDayReservationDTO = new EventDayReservationDTO();
        eventDayReservationDTO.setEventDayId(1L);
        eventDayReservationDTO.setPurchase(false);
        SeatDTO seatDTO = new SeatDTO();
        seatDTO.setCol(1);
        seatDTO.setRow(1);
        seatDTO.setSectorId(2L);

        eventDayReservationDTO.setSeats(new ArrayList<>(Collections.singletonList(seatDTO)));

        ParterDTO parterDTO = new ParterDTO();
        parterDTO.setNumberOfTickets(1);
        parterDTO.setSectorId(1L);

        eventDayReservationDTO.setParters(new ArrayList<>(Collections.singletonList(parterDTO)));

        return eventDayReservationDTO;

    }


    // PARTER

    @Test
    public void reserve_parter_success() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty, EventdayNotFound {

        Location location = generateLocation();
        Sector sector1 = generateSector_Parter();
        LocationSector locationSector1 = generateLocationSector_Parter();
        Ticket ticket_Parter = generateTicket_Parter();
        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);

        when(locationSectorServiceMocked.get(location.getId())).thenReturn(new ArrayList<>(Collections.singletonList(locationSector1)));
        when(ticketServiceMocked.getAvailableTicketsForEventDayAndSector(eventDay.getId(), sector1.getId())).thenReturn(new ArrayList<>(Collections.singletonList(ticket_Parter)));

        ticket_Parter.setUser(user);

        when(ticketRepositoryMocked.save(Mockito.any(Ticket.class))).thenReturn(ticket_Parter);
        when(ticketServiceMocked.getAvailableTickets(eventDay.getId())).thenReturn(new ArrayList<>());

        List<Ticket> reservedTickets = eventService.reserve(eventDayReservationDTO_Parter, user);

        assertEquals(1, reservedTickets.size());
        Ticket reservedTicket = reservedTickets.get(0);
        assertEquals(user.getId(), reservedTicket.getUser().getId());
        assertEquals(0, reservedTicket.getSeatCol());
        assertEquals(0, reservedTicket.getSeatRow());
        assertEquals(sector1.getId(), reservedTicket.getSectorId());
        assertEquals(eventDay.getId(), reservedTicket.getEventDay().getId());
        assertFalse(reservedTicket.isSold());

    }

    // Buy
    @Test
    public void buy_parter_success() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        Location location = generateLocation();
        Sector sector1 = generateSector_Parter();
        LocationSector locationSector1 = generateLocationSector_Parter();
        Ticket ticket_Parter = generateTicket_Parter();
        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();
        eventDayReservationDTO_Parter.setPurchase(true);

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);

        when(locationSectorServiceMocked.get(location.getId())).thenReturn(new ArrayList<>(Collections.singletonList(locationSector1)));
        when(ticketServiceMocked.getAvailableTicketsForEventDayAndSector(eventDay.getId(), sector1.getId())).thenReturn(new ArrayList<>(Collections.singletonList(ticket_Parter)));

        ticket_Parter.setUser(user);

        when(ticketRepositoryMocked.save(Mockito.any(Ticket.class))).thenReturn(ticket_Parter);
        when(ticketServiceMocked.getAvailableTickets(eventDay.getId())).thenReturn(new ArrayList<>());

        List<Ticket> reservedTickets = eventService.reserve(eventDayReservationDTO_Parter, user);

        assertEquals(1, reservedTickets.size());
        Ticket reservedTicket = reservedTickets.get(0);
        assertEquals(user.getId(), reservedTicket.getUser().getId());
        assertEquals(0, reservedTicket.getSeatCol());
        assertEquals(0, reservedTicket.getSeatRow());
        assertEquals(sector1.getId(), reservedTicket.getSectorId());
        assertEquals(eventDay.getId(), reservedTicket.getEventDay().getId());
        assertTrue(reservedTicket.isSold());

    }

    @Test(expected = EventDayDoesNotExistOrStateIsNotValid.class)
    public void reserve_parter_EventDayDoesNotExistOrStateIsNotValid() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();


        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenThrow(new EventDayDoesNotExistOrStateIsNotValid(eventDay.getId()));

        eventService.reserve(eventDayReservationDTO_Parter, user);

    }

    @Test(expected = SectorNotFound.class)
    public void reserve_parter_SectorNotFound() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        Location location = generateLocation();
        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);

        when(locationSectorServiceMocked.get(location.getId())).thenReturn(new ArrayList<>());

        eventService.reserve(eventDayReservationDTO_Parter, user);
    }

    @Test(expected = SectorWrongType.class)
    public void reserve_parter_SectorWrongType() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        Location location = generateLocation();
        LocationSector locationSector1 = generateLocationSector_Parter();
        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);

        locationSector1.getSector().setType(SectorType.GRANDSTAND);

        when(locationSectorServiceMocked.get(location.getId())).thenReturn(new ArrayList<>(Collections.singletonList(locationSector1)));

        eventService.reserve(eventDayReservationDTO_Parter, user);
    }

    @Test(expected = LocationSectorsDoesNotExistForLocation.class)
    public void reserve_parter_LocationSectorsDoesNotExistForLocation() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        Location location = generateLocation();
        LocationSector locationSector1 = generateLocationSector_Parter();
        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);

        locationSector1.setId(10L);

        when(locationSectorServiceMocked.get(location.getId())).thenThrow(new LocationSectorsDoesNotExistForLocation(locationSector1.getId()));

        eventService.reserve(eventDayReservationDTO_Parter, user);
    }

    @Test(expected = NumberOfTicketsException.class)
    public void reserve_parter_NumberOfTicketsException() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        Location location = generateLocation();
        Sector sector1 = generateSector_Parter();
        LocationSector locationSector1 = generateLocationSector_Parter();
        Ticket ticket_Parter = generateTicket_Parter();
        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();

        eventDayReservationDTO_Parter.getParters().get(0).setNumberOfTickets(10);

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);
        when(locationSectorServiceMocked.get(location.getId())).thenReturn(new ArrayList<>(Collections.singletonList(locationSector1)));
        when(ticketServiceMocked.getAvailableTicketsForEventDayAndSector(eventDay.getId(), sector1.getId())).thenReturn(new ArrayList<>(Collections.singletonList(ticket_Parter)));

        eventService.reserve(eventDayReservationDTO_Parter, user);
    }

    @Test(expected = ReservationIsNotPossible.class)
    public void reserve_parter_ReservationIsNotPossible() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        eventDay.setReservationExpirationDate(calendar.getTime());

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);

        eventService.reserve(eventDayReservationDTO_Parter, user);
    }

    // GRANDSTAND

    @Test
    public void reserve_grandstand_success() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        Location location = generateLocation();
        LocationSector locationSector2 = generateLocationSector_Grandstand();
        Ticket ticket_Grandstand = generateTicket_Grandstand();
        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);

        when(locationSectorServiceMocked.get(location.getId())).thenReturn(new ArrayList<>(Collections.singletonList(locationSector2)));
        when(ticketServiceMocked.getAvailableGrandstandTicketForEventDayAndSector(Mockito.any(SeatDTO.class), Mockito.any(EventDay.class))).thenReturn(ticket_Grandstand);

        ticket_Grandstand.setUser(user);

        when(ticketRepositoryMocked.save(Mockito.any(Ticket.class))).thenReturn(ticket_Grandstand);

        when(ticketServiceMocked.getAvailableTickets(eventDay.getId())).thenReturn(new ArrayList<>());

        List<Ticket> reservedTickets = eventService.reserve(eventDayReservationDTO_Grandstand, user);

        assertEquals(1, reservedTickets.size());
        Ticket reservedTicket = reservedTickets.get(0);
        assertEquals(user.getId(), reservedTicket.getUser().getId());
        assertEquals(eventDayReservationDTO_Grandstand.getSeats().get(0).getCol(), reservedTicket.getSeatCol());
        assertEquals(eventDayReservationDTO_Grandstand.getSeats().get(0).getRow(), reservedTicket.getSeatRow());
        assertEquals(eventDayReservationDTO_Grandstand.getSeats().get(0).getSectorId(), reservedTicket.getSectorId());
        assertEquals(eventDay.getId(), reservedTicket.getEventDay().getId());
        assertFalse(reservedTicket.isSold());
    }

    // Buy
    @Test
    public void buy_grandstand_success() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        Location location = generateLocation();
        LocationSector locationSector2 = generateLocationSector_Grandstand();
        Ticket ticket_Grandstand = generateTicket_Grandstand();
        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();
        eventDayReservationDTO_Grandstand.setPurchase(true);

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);

        when(locationSectorServiceMocked.get(location.getId())).thenReturn(new ArrayList<>(Collections.singletonList(locationSector2)));
        when(ticketServiceMocked.getAvailableGrandstandTicketForEventDayAndSector(Mockito.any(SeatDTO.class), Mockito.any(EventDay.class))).thenReturn(ticket_Grandstand);

        ticket_Grandstand.setUser(user);

        when(ticketRepositoryMocked.save(Mockito.any(Ticket.class))).thenReturn(ticket_Grandstand);

        when(ticketServiceMocked.getAvailableTickets(eventDay.getId())).thenReturn(new ArrayList<>());

        List<Ticket> reservedTickets = eventService.reserve(eventDayReservationDTO_Grandstand, user);

        assertEquals(1, reservedTickets.size());
        Ticket reservedTicket = reservedTickets.get(0);
        assertEquals(user.getId(), reservedTicket.getUser().getId());
        assertEquals(eventDayReservationDTO_Grandstand.getSeats().get(0).getCol(), reservedTicket.getSeatCol());
        assertEquals(eventDayReservationDTO_Grandstand.getSeats().get(0).getRow(), reservedTicket.getSeatRow());
        assertEquals(eventDayReservationDTO_Grandstand.getSeats().get(0).getSectorId(), reservedTicket.getSectorId());
        assertEquals(eventDay.getId(), reservedTicket.getEventDay().getId());
        assertTrue(reservedTicket.isSold());
    }

    @Test(expected = EventDayDoesNotExistOrStateIsNotValid.class)
    public void reserve_grandstand_EventDayDoesNotExistOrStateIsNotValid() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenThrow(new EventDayDoesNotExistOrStateIsNotValid(eventDay.getId()));

        eventService.reserve(eventDayReservationDTO_Grandstand, user);

    }

    @Test(expected = SectorNotFound.class)
    public void reserve_grandstand_SectorNotFound() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        Location location = generateLocation();
        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);

        when(locationSectorServiceMocked.get(location.getId())).thenReturn(new ArrayList<>());

        eventService.reserve(eventDayReservationDTO_Grandstand, user);
    }

    @Test(expected = SectorWrongType.class)
    public void reserve_grandstand_SectorWrongType() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        Location location = generateLocation();
        LocationSector locationSector2 = generateLocationSector_Grandstand();
        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);

        locationSector2.getSector().setType(SectorType.PARTER);

        when(locationSectorServiceMocked.get(location.getId())).thenReturn(new ArrayList<>(Collections.singletonList(locationSector2)));

        eventService.reserve(eventDayReservationDTO_Grandstand, user);
    }

    @Test(expected = LocationSectorsDoesNotExistForLocation.class)
    public void reserve_grandstand_LocationSectorsDoesNotExistForLocation() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        Location location = generateLocation();
        LocationSector locationSector2 = generateLocationSector_Grandstand();
        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);

        locationSector2.setId(10L);

        when(locationSectorServiceMocked.get(location.getId())).thenThrow(new LocationSectorsDoesNotExistForLocation(locationSector2.getId()));

        eventService.reserve(eventDayReservationDTO_Grandstand, user);
    }

    @Test(expected = SeatIsNotAvailable.class)
    public void reserve_grandstand_SeatIsNotAvailable() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        Location location = generateLocation();
        Sector sector2 = generateSector_Grandstand();
        LocationSector locationSector2 = generateLocationSector_Grandstand();
        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();

        SeatDTO seatDTO2 = new SeatDTO();
        seatDTO2.setSectorId(sector2.getId());
        seatDTO2.setRow(2);
        seatDTO2.setCol(1);

        SeatDTO seatDTO3 = new SeatDTO();
        seatDTO3.setSectorId(sector2.getId());
        seatDTO3.setRow(2);
        seatDTO3.setCol(2);

        eventDayReservationDTO_Grandstand.getSeats().add(seatDTO2);
        eventDayReservationDTO_Grandstand.getSeats().add(seatDTO3);


        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);
        when(locationSectorServiceMocked.get(location.getId())).thenReturn(new ArrayList<>(Collections.singletonList(locationSector2)));

        when(ticketServiceMocked.getAvailableGrandstandTicketForEventDayAndSector(Mockito.any(SeatDTO.class), Mockito.any(EventDay.class))).thenThrow(new SeatIsNotAvailable(seatDTO3));

        eventService.reserve(eventDayReservationDTO_Grandstand, user);
    }

    @Test(expected = ReservationIsNotPossible.class)
    public void reserve_grandstand_ReservationIsNotPossible() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        eventDay.setReservationExpirationDate(calendar.getTime());

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);


        eventService.reserve(eventDayReservationDTO_Grandstand, user);
    }


    // PARTER AND GRANDSTAND

    @Test
    public void reserve_parter_and_grandstand_success() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        Location location = generateLocation();
        Sector sector1 = generateSector_Parter();
        Sector sector2 = generateSector_Grandstand();
        LocationSector locationSector1 = generateLocationSector_Parter();
        LocationSector locationSector2 = generateLocationSector_Grandstand();
        Ticket ticket_Parter = generateTicket_Parter();
        Ticket ticket_Grandstand = generateTicket_Grandstand();
        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter_and_Grandstand();

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);

        when(locationSectorServiceMocked.get(location.getId())).thenReturn(new ArrayList<>(Arrays.asList(locationSector1, locationSector2)));

        when(ticketServiceMocked.getAvailableTicketsForEventDayAndSector(eventDay.getId(), sector1.getId())).thenReturn(new ArrayList<>(Collections.singletonList(ticket_Parter)));
        when(ticketServiceMocked.getAvailableGrandstandTicketForEventDayAndSector(Mockito.any(SeatDTO.class), Mockito.any(EventDay.class))).thenReturn(ticket_Grandstand);


        ticket_Grandstand.setUser(user);
        ticket_Parter.setUser(user);

        when(ticketRepositoryMocked.save(Mockito.any(Ticket.class))).thenReturn(ticket_Parter, ticket_Grandstand);

        when(ticketServiceMocked.getAvailableTickets(eventDay.getId())).thenReturn(new ArrayList<>());

        List<Ticket> reservedTickets = eventService.reserve(eventDayReservationDTO, user);


        assertEquals(2, reservedTickets.size());

        Ticket reservedTicket_Parter = reservedTickets.get(1);

        assertEquals(user.getId(), reservedTicket_Parter.getUser().getId());
        assertEquals(0, reservedTicket_Parter.getSeatCol());
        assertEquals(0, reservedTicket_Parter.getSeatRow());
        assertEquals(sector1.getId(), reservedTicket_Parter.getSectorId());
        assertEquals(eventDay.getId(), reservedTicket_Parter.getEventDay().getId());
        assertFalse(reservedTickets.get(1).isSold());

        Ticket reservedTicket_Grandstand = reservedTickets.get(0);

        assertEquals(user.getId(), reservedTicket_Grandstand.getUser().getId());
        assertEquals(eventDayReservationDTO.getSeats().get(0).getCol(), reservedTicket_Grandstand.getSeatCol());
        assertEquals(eventDayReservationDTO.getSeats().get(0).getRow(), reservedTicket_Grandstand.getSeatRow());
        assertEquals(eventDayReservationDTO.getSeats().get(0).getSectorId(), reservedTicket_Grandstand.getSectorId());
        assertEquals(eventDay.getId(), reservedTicket_Grandstand.getEventDay().getId());
        assertFalse(reservedTickets.get(0).isSold());
    }


    // Buy
    @Test
    public void buy_parter_and_grandstand_success() throws EventdayNotFound, EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        Location location = generateLocation();
        Sector sector1 = generateSector_Parter();
        Sector sector2 = generateSector_Grandstand();
        LocationSector locationSector1 = generateLocationSector_Parter();
        LocationSector locationSector2 = generateLocationSector_Grandstand();
        Ticket ticket_Parter = generateTicket_Parter();
        Ticket ticket_Grandstand = generateTicket_Grandstand();
        User user = generateUser();
        EventDay eventDay = generateEventDay();

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter_and_Grandstand();
        eventDayReservationDTO.setPurchase(true);

        when(eventDayServiceMocked.getReservableAndBuyableAndDateAfter(Mockito.anyLong(), Mockito.any(Date.class))).thenReturn(eventDay);

        when(locationSectorServiceMocked.get(location.getId())).thenReturn(new ArrayList<>(Arrays.asList(locationSector1, locationSector2)));

        when(ticketServiceMocked.getAvailableTicketsForEventDayAndSector(eventDay.getId(), sector1.getId())).thenReturn(new ArrayList<>(Collections.singletonList(ticket_Parter)));
        when(ticketServiceMocked.getAvailableGrandstandTicketForEventDayAndSector(Mockito.any(SeatDTO.class), Mockito.any(EventDay.class))).thenReturn(ticket_Grandstand);


        ticket_Grandstand.setUser(user);
        ticket_Parter.setUser(user);

        when(ticketRepositoryMocked.save(Mockito.any(Ticket.class))).thenReturn(ticket_Parter, ticket_Grandstand);

        when(ticketServiceMocked.getAvailableTickets(eventDay.getId())).thenReturn(new ArrayList<>());

        List<Ticket> reservedTickets = eventService.reserve(eventDayReservationDTO, user);


        assertEquals(2, reservedTickets.size());

        Ticket reservedTicket_Parter = reservedTickets.get(1);

        assertEquals(user.getId(), reservedTicket_Parter.getUser().getId());
        assertEquals(0, reservedTicket_Parter.getSeatCol());
        assertEquals(0, reservedTicket_Parter.getSeatRow());
        assertEquals(sector1.getId(), reservedTicket_Parter.getSectorId());
        assertEquals(eventDay.getId(), reservedTicket_Parter.getEventDay().getId());
        assertTrue(reservedTickets.get(1).isSold());

        Ticket reservedTicket_Grandstand = reservedTickets.get(0);

        assertEquals(user.getId(), reservedTicket_Grandstand.getUser().getId());
        assertEquals(eventDayReservationDTO.getSeats().get(0).getCol(), reservedTicket_Grandstand.getSeatCol());
        assertEquals(eventDayReservationDTO.getSeats().get(0).getRow(), reservedTicket_Grandstand.getSeatRow());
        assertEquals(eventDayReservationDTO.getSeats().get(0).getSectorId(), reservedTicket_Grandstand.getSectorId());
        assertEquals(eventDay.getId(), reservedTicket_Grandstand.getEventDay().getId());
        assertTrue(reservedTickets.get(0).isSold());
    }

}
