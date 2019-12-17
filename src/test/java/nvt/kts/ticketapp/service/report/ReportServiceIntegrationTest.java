package nvt.kts.ticketapp.service.report;

import nvt.kts.ticketapp.ClearDatabaseRule;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.aspectj.runtime.internal.Conversions.intValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportServiceIntegrationTest {

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private ReportsService reportService;

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
    public void setUp() throws ParseException {
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
     * Test report about event with id EXISTING_EVENT_ID == 1L
     *
     * @throws EventNotFound
     */
    @Test
    public void eventReport_Positive() throws EventNotFound {
        double totalIncome = ticket_01.getPrice() + ticket_02.getPrice() +
                ticket_03.getPrice() + ticket_04.getPrice() + ticket_05.getPrice();
        double avgPrice = totalIncome / 5;

        EventReportDTO eventReport = reportService.eventReport(daysOfTown.getId());

        assertNotNull(eventReport);
        assertEquals(5, eventReport.getNumOfTickets());
        assertEquals(6, eventReport.getNumOfReservations());
        assertEquals(intValue(totalIncome), intValue(eventReport.getTotalIncome()), 0.001);
        assertEquals(intValue(avgPrice), intValue(eventReport.getAvgPrice()), 0.001);
    }

    /**
     * Test report about event with NONEXISTENT_EVENT_ID == 2L fails
     *
     * @throws EventNotFound
     */
    @Test(expected = EventNotFound.class)
    public void eventReport_Negative_EventNotFound() throws EventNotFound {
        reportService.eventReport(NONEXISTENT_EVENT_ID);
    }

    /**
     * Test report about event days of a single event with id EXISTING_EVENT_ID == 1L
     *
     * @throws EventNotFound
     */
    @Test
    public void eventDaysReport_Positive() throws EventNotFound {
        List<EventDayReportDTO> eventDayReports = reportService.eventDaysReport(daysOfTown.getId());

        assertNotNull(eventDayReports);
        assertEquals(5, eventDayReports.size());
        assertEquals(intValue(20.0), intValue(eventDayReports.get(0).getTotalIncome()));
        assertEquals(1, eventDayReports.get(0).getNumOfTickets());
        assertEquals(3, eventDayReports.get(4).getNumOfReservations());
        assertEquals(1, intValue(eventDayReports.get(4).getSoldBySector().get(west.getId())));
        assertEquals(0, intValue(eventDayReports.get(4).getSoldBySector().get(east.getId())));
    }

    /**
     * Test report about event days of a single event with id NONEXISTENT_EVENT_ID == 2L fails
     *
     * @throws EventNotFound
     */
    @Test(expected = EventNotFound.class)
    public void eventDaysReport_Negative_EventNotFound() throws EventNotFound {
        reportService.eventDaysReport(NONEXISTENT_EVENT_ID);
    }

    /**
     * Test report about events on a single location with id EXISTING_LOCATION_ID == 1L
     *
     * @throws LocationNotFound
     */
    @Test
    public void locationReport_Positive() throws LocationNotFound {
        double totalIncome = ticket_01.getPrice() + ticket_02.getPrice() +
                ticket_03.getPrice() + ticket_04.getPrice() + ticket_05.getPrice();
        LocationReportDTO locationReport = reportService.locationReport(cityHall.getId());

        assertNotNull(locationReport);
        assertEquals(intValue(totalIncome), intValue(locationReport.getTotalIncome()), 0.001);
        assertEquals(5, intValue(locationReport.getNumOfEventDaysByCategory().get(EventCategory.ENTERTAINMENT)));
        assertEquals(0, intValue(locationReport.getNumOfEventDaysByCategory().get(EventCategory.CULTURAL)));
    }

    /**
     * Test report about location with id NONEXISTENT_LOCATION_ID == 2L fails
     *
     * @throws LocationNotFound
     */
    @Test(expected = LocationNotFound.class)
    public void locationReport_Negative_LocationNotFound() throws LocationNotFound {
        reportService.locationReport(NONEXISTENT_LOCATION_ID);
    }
}
