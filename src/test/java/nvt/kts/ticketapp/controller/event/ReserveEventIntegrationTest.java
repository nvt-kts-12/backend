package nvt.kts.ticketapp.controller.event;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.event.EventDayReservationDTO;
import nvt.kts.ticketapp.domain.dto.event.ParterDTO;
import nvt.kts.ticketapp.domain.dto.event.SeatDTO;
import nvt.kts.ticketapp.domain.dto.ticket.TicketDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class ReserveEventIntegrationTest {

    @Autowired
    private TestRestTemplate clientTemplate;

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    private String url = "/api/event/reserve";

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

    @PostConstruct
    public void postConstruct() {
        this.clientTemplate = this.clientTemplate.withBasicAuth("username", "password");
    }

    @Before
    public void initDatabase() {

        User user = new User("username", "password", "firstname", "lastname", "email@gmail.com");
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

    @Test
    public void reserve_success() {
        ResponseEntity response = clientTemplate.postForEntity(url, generateEventDayReservationDTO_Parter(), TicketDTO.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void reserve_LocationSectorsDoesNotExistForLocation() {
        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        LocationScheme locationScheme = locationSchemeRepository.getOne(1L);
        Location location = new Location(locationScheme);
        locationRepository.save(location);

        eventDay.get().setLocation(location);
        eventDaysRepository.save(eventDay.get());

        ResponseEntity response = clientTemplate.postForEntity(url, generateEventDayReservationDTO_Parter(), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void reserve_SectorNotFound() {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter();
        eventDayReservationDTO.getParters().get(0).setSectorId(10L);

        ResponseEntity response = clientTemplate.postForEntity(url, eventDayReservationDTO, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void reserve_SectorWrongType() {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter();
        eventDayReservationDTO.getParters().get(0).setSectorId(2L);

        ResponseEntity response = clientTemplate.postForEntity(url, eventDayReservationDTO, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void reserve_EventDayDoesNotExistOrStateIsNotValid_EventDayNotExist() {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter();
        eventDayReservationDTO.getParters().get(0).setSectorId(2L);

        ResponseEntity response = clientTemplate.postForEntity(url, eventDayReservationDTO, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void reserve_EventDayDoesNotExist_DateAfter(){


        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter();
        eventDayReservationDTO.getParters().get(0).setSectorId(2L);

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        eventDay.get().setDate(calendar.getTime());
        eventDaysRepository.save(eventDay.get());

        ResponseEntity response = clientTemplate.postForEntity(url, eventDayReservationDTO, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void reserve_StateIsNotValid() {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter();

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);
        eventDay.get().setState(EventDayState.NOT_IN_SALE);
        eventDaysRepository.save(eventDay.get());

        ResponseEntity response = clientTemplate.postForEntity(url, eventDayReservationDTO, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void reserve_NumberOfTicketsException() {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Parter();
        eventDayReservationDTO.getParters().get(0).setNumberOfTickets(10);

        ResponseEntity response = clientTemplate.postForEntity(url, eventDayReservationDTO, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void reserve_SeatIsNotAvailable() {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Grandstand();

        Optional<Ticket> ticket = ticketRepository.findOneById(2L);
        User user = new User("anotherUser", "pass", "firstname", "lastname", "email2@gmail.com");
        userRepository.save(user);
        ticket.get().setUser(user);
        ticketRepository.save(ticket.get());

        ResponseEntity response = clientTemplate.postForEntity(url, eventDayReservationDTO, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void reserve_ReservationIsNotPossible() {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Grandstand();

        Optional<EventDay> eventDay = eventDaysRepository.findById(1L);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        eventDay.get().setReservationExpirationDate(calendar.getTime());
        eventDaysRepository.save(eventDay.get());

        ResponseEntity response = clientTemplate.postForEntity(url, eventDayReservationDTO, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void reserve_UserNotFound() {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Grandstand();

        userRepository.deleteAll();

        ResponseEntity response = clientTemplate.postForEntity(url, eventDayReservationDTO, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void reserve_ObjectOptimisticLockingFailureException() {

        EventDayReservationDTO eventDayReservationDTO = generateEventDayReservationDTO_Grandstand();
        EventDayReservationDTO eventDayReservationDTO2 = generateEventDayReservationDTO_Grandstand();


        ResponseEntity response = clientTemplate.postForEntity(url, eventDayReservationDTO, String.class);
        ResponseEntity response2 = clientTemplate.postForEntity(url, eventDayReservationDTO2, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response2.getBody());
        assertEquals(HttpStatus.EXPECTATION_FAILED, response2.getStatusCode());

    }



}
