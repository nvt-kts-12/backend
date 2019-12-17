package nvt.kts.ticketapp.controller.report;


import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.PlainEventDayDTO;
import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.dto.location.PlainLocationDTO;
import nvt.kts.ticketapp.domain.dto.report.EventDayReportDTO;
import nvt.kts.ticketapp.domain.dto.report.EventReportDTO;
import nvt.kts.ticketapp.domain.dto.report.LocationReportDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.exception.location.LocationNotFound;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.aspectj.runtime.internal.Conversions.intValue;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class ReportsControllerIntegrationTest {

    private static final String URL_PREFIX = "/api/reports";

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private TestRestTemplate restTemplate;


    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventDaysRepository eventDaysRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private LocationSectorRepository locationSectorRepository;
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationSchemeRepository locationSchemeRepository;
    @Autowired
    private UserRepository userRepository;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static Long NONEXISTENT_EVENT_ID = 5L;
    private static Long NONEXISTENT_LOCATION_ID = 5L;

    private LocationScheme cityHallScheme;
    private Location cityHall;
    private Sector eastGrandstand;
    private Sector westGrandstand;
    private LocationSector east;
    private LocationSector west;
    private static Event daysOfTown;
    private static EventDay day_01;
    private static EventDay day_02;
    private static EventDay day_03;
    private static EventDay day_04;
    private static EventDay day_05;
    private static Ticket ticket_01;
    private static Ticket ticket_02;
    private static Ticket ticket_03;
    private static Ticket ticket_04;
    private static Ticket ticket_05;
    private static Ticket reservation_01;
    private static Ticket reservation_02;
    private static Ticket reservation_03;
    private static Ticket reservation_04;
    private static Ticket reservation_05;
    private static Ticket reservation_06;
    private static User user;

    @Before
    public void setUp() throws Exception {
        cityHallScheme = locationSchemeRepository.save(new LocationScheme("Spens", "Maksima Gorkog."));
        cityHall = locationRepository.save(new Location(cityHallScheme));
        daysOfTown = eventRepository.save(new Event("Dani grada", EventCategory.ENTERTAINMENT, "Bunch of manifestations."));

        day_01 = eventDaysRepository.save(new EventDay(sdf.parse("2015-06-26"), cityHall, sdf.parse("2015-06-20"),
                EventDayState.RESERVABLE_AND_BUYABLE, daysOfTown));
        day_02 = eventDaysRepository.save(new EventDay(sdf.parse("2015-06-27"), cityHall, sdf.parse("2015-06-20"),
                EventDayState.RESERVABLE_AND_BUYABLE, daysOfTown));
        day_03 = eventDaysRepository.save(new EventDay(sdf.parse("2015-06-28"), cityHall, sdf.parse("2015-06-20"),
                EventDayState.RESERVABLE_AND_BUYABLE, daysOfTown));
        day_04 = eventDaysRepository.save(new EventDay(sdf.parse("2015-06-29"), cityHall, sdf.parse("2015-06-20"),
                EventDayState.RESERVABLE_AND_BUYABLE, daysOfTown));
        day_05 = eventDaysRepository.save(new EventDay(sdf.parse("2015-06-30"), cityHall, sdf.parse("2015-06-20"),
                EventDayState.RESERVABLE_AND_BUYABLE, daysOfTown));

        eastGrandstand = sectorRepository.save(new Sector(10.0, 10.0, 15.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, cityHallScheme));
        westGrandstand = sectorRepository.save(new Sector(0.0, 10.0, 5.0, 0.0, 100,
                10, 10, SectorType.GRANDSTAND, cityHallScheme));

        user = userRepository.save(new User("username", "password", "User",
                "Userkovic", "userkovic@gmail.com"));

        east = locationSectorRepository.save(new LocationSector(eastGrandstand, cityHall, 20.0, 100, false));
        west = locationSectorRepository.save(new LocationSector(westGrandstand, cityHall, 80.0, 100, true));

        ticket_01 = ticketRepository.save(new Ticket(true, east.getId(), 1, 1, 20.0, day_01, user, false));
        ticket_02 = ticketRepository.save(new Ticket(true, west.getId(), 2, 1, 80.0, day_02, user, true));
        ticket_03 = ticketRepository.save(new Ticket(true, east.getId(), 3, 1, 20.0, day_03, user, false));
        ticket_04 = ticketRepository.save(new Ticket(true, east.getId(), 4, 1, 20.0, day_04, user, false));
        ticket_05 = ticketRepository.save(new Ticket(true, west.getId(), 5, 1, 80.0, day_05, user, true));


        reservation_01 = ticketRepository.save(new Ticket(false, east.getId(), 7, 1, 20.0, day_01, user, false));
        reservation_02 = ticketRepository.save(new Ticket(false, west.getId(), 5, 8, 80.0, day_05, user, true));
        reservation_03 = ticketRepository.save(new Ticket(false, east.getId(), 5, 9, 20.0, day_05, user, false));
        reservation_04 = ticketRepository.save(new Ticket(false, west.getId(), 5, 10, 80.0, day_02, user, true));
        reservation_05 = ticketRepository.save(new Ticket(false, west.getId(), 2, 2, 80.0, day_03, user, true));
        reservation_06 = ticketRepository.save(new Ticket(false, east.getId(), 3, 3, 20.0, day_05, user, false));
    }

    /**
     * Test report about event with id 1L
     *
     * @throws EventNotFound
     */
    @Test
    public void eventReport_Positive() throws EventNotFound {
        double totalIncome = ticket_01.getPrice() + ticket_02.getPrice() + ticket_03.getPrice() +
                ticket_04.getPrice() + ticket_05.getPrice();
        ResponseEntity<EventReportDTO> response = restTemplate.withBasicAuth("admin", "password")
                .getForEntity(URL_PREFIX + "/event/" + daysOfTown.getId(), EventReportDTO.class);

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getEventDTO());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(daysOfTown.getId(), response.getBody().getEventDTO().getId());
        assertEquals(5, response.getBody().getNumOfTickets());
        assertEquals(6, response.getBody().getNumOfReservations());
        assertEquals(intValue(totalIncome), intValue(response.getBody().getTotalIncome()), 0.001);

    }

    /**
     * Test report about event with id 2L fails because there is no event with such id
     *
     * @throws EventNotFound
     */
    @Test
    public void eventReport_Negative_EventNotFound() throws EventNotFound {
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin", "password")
                .getForEntity(URL_PREFIX + "/event/" + NONEXISTENT_EVENT_ID, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    /**
     * Test report about every day of an single event with id 1L
     *
     * @throws EventNotFound
     */
    @Test
    public void eventDaysReport() throws EventNotFound {
        ResponseEntity<List<EventDayReportDTO>> response = restTemplate.withBasicAuth("admin", "password")
                .exchange
                        (URL_PREFIX + "/eventDay/" + daysOfTown.getId(), HttpMethod.GET, null,
                                new ParameterizedTypeReference<List<EventDayReportDTO>>() {
                        });

        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().size());
        assertEquals(day_01.getId(), response.getBody().get(0).getEventDayDTO().getId());
        assertEquals(day_03.getId(), response.getBody().get(2).getEventDayDTO().getId());
    }

    /**
     * Test report about every day of an single event fails because event with id 2L does not exist
     *
     * @throws EventNotFound
     */
    @Test
    public void eventDaysReport_Negative_EventNotFound() throws EventNotFound {
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin", "password")
                .getForEntity(URL_PREFIX + "/eventDay/" + NONEXISTENT_EVENT_ID, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test report about events on single location with id 1L
     *
     * @throws LocationNotFound
     */
    @Test
    public void locationReport_Positive() throws LocationNotFound {
        ResponseEntity<LocationReportDTO> response = restTemplate.withBasicAuth("admin", "password")
                .getForEntity(URL_PREFIX + "/location/" + cityHall.getId(), LocationReportDTO.class);

        assertNotNull(response);
        assertEquals(daysOfTown.getId(), response.getBody().getLocationDTO().getLocationSchemeId());
        assertEquals(5, intValue(response.getBody().getNumOfEventDaysByCategory().get(EventCategory.ENTERTAINMENT)));
        assertEquals(0, intValue(response.getBody().getNumOfEventDaysByCategory().get(EventCategory.CULTURAL)));
    }

    /**
     * Test report about events on single location fails because location with id 2L does not exist
     *
     * @throws LocationNotFound
     */
    @Test
    public void locationReport_Negative_LocationNotFound() throws LocationNotFound {
        ResponseEntity<String> response = restTemplate.withBasicAuth("admin", "password")
                .getForEntity(URL_PREFIX + "/location/" + NONEXISTENT_LOCATION_ID, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
