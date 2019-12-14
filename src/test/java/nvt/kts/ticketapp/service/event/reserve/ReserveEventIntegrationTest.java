package nvt.kts.ticketapp.service.event.reserve;

import com.google.zxing.WriterException;
import nvt.kts.ticketapp.ClearDatabaseRule;
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
import nvt.kts.ticketapp.exception.location.LocationSectorsDoesNotExistForLocation;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import nvt.kts.ticketapp.exception.sector.SectorWrongType;
import nvt.kts.ticketapp.exception.ticket.NumberOfTicketsException;
import nvt.kts.ticketapp.exception.ticket.ReservationIsNotPossible;
import nvt.kts.ticketapp.exception.ticket.SeatIsNotAvailable;
import nvt.kts.ticketapp.exception.ticket.TicketListCantBeEmpty;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import nvt.kts.ticketapp.service.event.EventService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReserveEventIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private EventService eventService;

    private User user;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private LocationSectorRepository locationSectorRepository;
    @Autowired
    private LocationSchemeRepository locationSchemeRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventDaysRepository eventDaysRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Before
    public void initDatabase() {

        user = new User("username", "password", "firstname", "lastname", "email@gmail.com");
        userRepository.save(user);

        LocationScheme locationScheme = new LocationScheme("Scheme", "Address");
        locationSchemeRepository.save(locationScheme);

        Location location = new Location(locationScheme);
        locationRepository.save(location);

        Sector sectorParter = new Sector(0.0, 0.0, 0.0, 0.0, 1, 0, 0, SectorType.PARTER, locationScheme);
        sectorRepository.save(sectorParter);

        Sector sectorGrandstand = new Sector(0.0, 0.0, 0.0, 0.0, 1, 1, 1, SectorType.GRANDSTAND, locationScheme);
        sectorRepository.save(sectorGrandstand);

        LocationSector locationSectorParter = new LocationSector(sectorParter, location, 500, 2, false);
        locationSectorRepository.save(locationSectorParter);

        LocationSector locationSectorGrandstand = new LocationSector(sectorGrandstand, location, 500, 2, false);
        locationSectorRepository.save(locationSectorGrandstand);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        Date date = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, -2);
        Date reservationExpirationDate = calendar.getTime();

        Event event = new Event("Event", EventCategory.ENTERTAINMENT, "desc");
        eventRepository.save(event);

        EventDay eventDay = new EventDay(date, location, reservationExpirationDate, EventDayState.RESERVABLE_AND_BUYABLE, event);
        eventDaysRepository.save(eventDay);

        Ticket ticketParter = new Ticket(false, sectorParter.getId(), 0, 0, 200, eventDay, null,false);
        Ticket ticketGrandstand = new Ticket(false, sectorGrandstand.getId(), 1, 1, 200, eventDay, null,false);

        ticketRepository.saveAll(Arrays.asList(ticketParter, ticketGrandstand));

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
    public void reserve_parter_success() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();

        eventService.reserve(eventDayReservationDTO_Parter, user);

        List<Ticket> tickets = ticketRepository.findAll();

        int reservedTickets = 0;
        Ticket reservedTicket = null;
        for (Ticket t: tickets) {
            if (t.getUser() != null) {
                reservedTickets++;
                reservedTicket = t;
            }
        }

        assertEquals(1, reservedTickets);
        assertNotNull(reservedTicket);
        assertNotNull(reservedTicket.getUser());
        assertEquals(user.getId(), reservedTicket.getUser().getId());
        assertFalse(reservedTicket.isSold());

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        assertTrue(eventDay.isPresent());
        assertEquals(EventDayState.RESERVABLE_AND_BUYABLE, eventDay.get().getState());

    }

    @Test
    public void buy_parter_success() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();
        eventDayReservationDTO_Parter.setPurchase(true);

        eventService.reserve(eventDayReservationDTO_Parter, user);

        List<Ticket> tickets = ticketRepository.findAll();

        int reservedTickets = 0;
        Ticket reservedTicket = null;
        for (Ticket t: tickets) {
            if (t.getUser() != null) {
                reservedTickets++;
                reservedTicket = t;
            }
        }

        assertEquals(1, reservedTickets);
        assertNotNull(reservedTicket);
        assertNotNull(reservedTicket.getUser());
        assertEquals(user.getId(), reservedTicket.getUser().getId());
        assertTrue(reservedTicket.isSold());

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        assertTrue(eventDay.isPresent());
        assertEquals(EventDayState.RESERVABLE_AND_BUYABLE, eventDay.get().getState());

    }

    @Test(expected = EventDayDoesNotExistOrStateIsNotValid.class)
    public void reserve_parter_EventDayDoesNotExist() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter();
        eventDayReservationDTO.setEventDayId(10L);

        eventService.reserve(eventDayReservationDTO, user);

    }

    @Test(expected = EventDayDoesNotExistOrStateIsNotValid.class)
    public void reserve_parter_EventDayDoesNotExist_DateAfter() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter();

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        eventDay.get().setDate(calendar.getTime());
        eventDaysRepository.save(eventDay.get());
        eventService.reserve(eventDayReservationDTO, user);

    }

    @Test(expected = EventDayDoesNotExistOrStateIsNotValid.class)
    public void reserve_parter_StateIsNotValid() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter();

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);
        eventDay.get().setState(EventDayState.NOT_IN_SALE);
        eventDaysRepository.save(eventDay.get());

        eventService.reserve(eventDayReservationDTO, user);

    }

    @Test(expected = SectorNotFound.class)
    public void reserve_parter_SectorNotFound() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {


        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();
        eventDayReservationDTO_Parter.getParters().get(0).setSectorId(10L);

        eventService.reserve(eventDayReservationDTO_Parter, user);
    }

    @Test(expected = SectorWrongType.class)
    public void reserve_parter_SectorWrongType() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {


        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();
        eventDayReservationDTO_Parter.getParters().get(0).setSectorId(2L);

        eventService.reserve(eventDayReservationDTO_Parter, user);
    }

    @Test(expected = LocationSectorsDoesNotExistForLocation.class)
    public void reserve_parter_LocationSectorsDoesNotExistForLocation() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        LocationScheme locationScheme = locationSchemeRepository.getOne(1L);
        Location location = new Location(locationScheme);
        locationRepository.save(location);

        eventDay.get().setLocation(location);
        eventDaysRepository.save(eventDay.get());

        eventService.reserve(eventDayReservationDTO_Parter, user);
    }

    @Test(expected = NumberOfTicketsException.class)
    public void reserve_parter_NumberOfTicketsException() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();

        eventDayReservationDTO_Parter.getParters().get(0).setNumberOfTickets(10);

        eventService.reserve(eventDayReservationDTO_Parter, user);
    }

    @Test(expected = ReservationIsNotPossible.class)
    public void reserve_parter_ReservationIsNotPossible() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {


        EventDayReservationDTO eventDayReservationDTO_Parter = generateEventDayReservationDTO_Parter();

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        eventDay.get().setReservationExpirationDate(calendar.getTime());

        eventDaysRepository.save(eventDay.get());

        eventService.reserve(eventDayReservationDTO_Parter, user);
    }

    // GRANDSTAND

    @Test
    public void reserve_grandstand_success() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {


        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();

        eventService.reserve(eventDayReservationDTO_Grandstand, user);

        List<Ticket> tickets = ticketRepository.findAll();

        int reservedTickets = 0;
        Ticket reservedTicket = null;
        for (Ticket t: tickets) {
            if (t.getUser() != null) {
                reservedTickets++;
                reservedTicket = t;
            }
        }

        assertEquals(1, reservedTickets);
        assertNotNull(reservedTicket);
        assertNotNull(reservedTicket.getUser());
        assertEquals(user.getId(), reservedTicket.getUser().getId());
        assertFalse(reservedTicket.isSold());

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        assertTrue(eventDay.isPresent());
        assertEquals(EventDayState.RESERVABLE_AND_BUYABLE, eventDay.get().getState());
    }

    @Test
    public void buy_grandstand_success() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {


        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();
        eventDayReservationDTO_Grandstand.setPurchase(true);

        eventService.reserve(eventDayReservationDTO_Grandstand, user);

        List<Ticket> tickets = ticketRepository.findAll();

        int reservedTickets = 0;
        Ticket reservedTicket = null;
        for (Ticket t: tickets) {
            if (t.getUser() != null) {
                reservedTickets++;
                reservedTicket = t;
            }
        }

        assertEquals(1, reservedTickets);
        assertNotNull(reservedTicket);
        assertNotNull(reservedTicket.getUser());
        assertEquals(user.getId(), reservedTicket.getUser().getId());
        assertTrue(reservedTicket.isSold());

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        assertTrue(eventDay.isPresent());
        assertEquals(EventDayState.RESERVABLE_AND_BUYABLE, eventDay.get().getState());
    }

    @Test(expected = EventDayDoesNotExistOrStateIsNotValid.class)
    public void reserve_grandstand_EventDayDoesNotExist() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter();
        eventDayReservationDTO.setEventDayId(10L);

        eventService.reserve(eventDayReservationDTO, user);

    }

    @Test(expected = EventDayDoesNotExistOrStateIsNotValid.class)
    public void reserve_grandstand_EventDayDoesNotExist_DateAfter() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter();

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        eventDay.get().setDate(calendar.getTime());
        eventDaysRepository.save(eventDay.get());
        eventService.reserve(eventDayReservationDTO, user);

    }

    @Test(expected = EventDayDoesNotExistOrStateIsNotValid.class)
    public void reserve_grandstand_StateIsNotValid() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter();

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);
        eventDay.get().setState(EventDayState.NOT_IN_SALE);
        eventDaysRepository.save(eventDay.get());

        eventService.reserve(eventDayReservationDTO, user);

    }

    @Test(expected = SectorNotFound.class)
    public void reserve_grandstand_SectorNotFound() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();
        eventDayReservationDTO_Grandstand.getSeats().get(0).setSectorId(10L);

        eventService.reserve(eventDayReservationDTO_Grandstand, user);
    }

    @Test(expected = SectorWrongType.class)
    public void reserve_grandstand_SectorWrongType() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();
        eventDayReservationDTO_Grandstand.getSeats().get(0).setSectorId(1L);

        eventService.reserve(eventDayReservationDTO_Grandstand, user);
    }

    @Test(expected = LocationSectorsDoesNotExistForLocation.class)
    public void reserve_grandstand_LocationSectorsDoesNotExistForLocation() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        LocationScheme locationScheme = locationSchemeRepository.getOne(1L);
        Location location = new Location(locationScheme);
        locationRepository.save(location);

        eventDay.get().setLocation(location);
        eventDaysRepository.save(eventDay.get());

        eventService.reserve(eventDayReservationDTO_Grandstand, user);
    }

    @Test(expected = SeatIsNotAvailable.class)
    public void reserve_grandstand_SeatIsNotAvailable() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();

        Optional<Ticket> ticket = ticketRepository.findOneById(2L);
        User user = new User("anotherUser", "pass", "firstname", "lastname", "email2@gmail.com");
        userRepository.save(user);
        ticket.get().setUser(user);
        ticketRepository.save(ticket.get());

        eventService.reserve(eventDayReservationDTO_Grandstand, user);
    }

    @Test(expected = ReservationIsNotPossible.class)
    public void reserve_grandstand_ReservationIsNotPossible() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO_Grandstand = generateEventDayReservationDTO_Grandstand();

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        eventDay.get().setReservationExpirationDate(calendar.getTime());
        eventDaysRepository.save(eventDay.get());

        eventService.reserve(eventDayReservationDTO_Grandstand, user);
    }

    // PARTER AND GRANDSTAND

    @Test
    public void reserve_parter_and_grandstand_success() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter_and_Grandstand();

        eventService.reserve(eventDayReservationDTO, user);

        List<Ticket> tickets = ticketRepository.findAll();

        int numberOfReservedTickets = 2;
        List<Ticket> reservedTickets = new ArrayList<>();
        for (Ticket t: tickets) {
            if (t.getUser() != null) {
                numberOfReservedTickets++;
                reservedTickets.add(t);
            }
        }

        assertEquals(2, reservedTickets.size());
        assertNotNull(reservedTickets.get(0).getUser());
        assertEquals(user.getId(), reservedTickets.get(0).getUser().getId());
        assertFalse(reservedTickets.get(0).isSold());
        assertEquals(user.getId(), reservedTickets.get(1).getUser().getId());
        assertFalse(reservedTickets.get(1).isSold());

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        assertTrue(eventDay.isPresent());
        assertEquals(EventDayState.SOLD_OUT, eventDay.get().getState());

    }

    @Test
    public void buy_parter_and_grandstand_success() throws EventDayDoesNotExistOrStateIsNotValid, SeatIsNotAvailable, SectorNotFound, SectorWrongType, LocationSectorsDoesNotExistForLocation, NumberOfTicketsException, WriterException, ReservationIsNotPossible, IOException, TicketListCantBeEmpty {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter_and_Grandstand();
        eventDayReservationDTO.setPurchase(true);

        eventService.reserve(eventDayReservationDTO, user);

        List<Ticket> tickets = ticketRepository.findAll();

        int numberOfReservedTickets = 2;
        List<Ticket> reservedTickets = new ArrayList<>();
        for (Ticket t: tickets) {
            if (t.getUser() != null) {
                numberOfReservedTickets++;
                reservedTickets.add(t);
            }
        }

        assertEquals(2, reservedTickets.size());
        assertNotNull(reservedTickets.get(0).getUser());
        assertEquals(user.getId(), reservedTickets.get(0).getUser().getId());
        assertTrue(reservedTickets.get(0).isSold());
        assertNotNull(reservedTickets.get(1).getUser());
        assertEquals(user.getId(), reservedTickets.get(1).getUser().getId());
        assertTrue(reservedTickets.get(1).isSold());

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        assertTrue(eventDay.isPresent());
        assertEquals(EventDayState.SOLD_OUT, eventDay.get().getState());

    }

}
