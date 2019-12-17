package nvt.kts.ticketapp.service.report;

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
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportsServiceUnitTest {

    private ReportsService reportsService;

    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventDaysRepository eventDaysRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private LocationSectorRepository locationSectorRepository;
    @Mock
    private LocationRepository locationRepository;

    private final Long EXISTING_EVENT_ID = 1L;
    private final Long EXISTING_LOCATION_ID = 1L;
    private final Long NONEXISTENT_LOCATION_ID = 2L;
    private final Long EXISTING_EVENT_DAY_ID = 1L;
    private final Long EXISTING_EVENT_DAY2_ID = 2L;
    private final Long NONEXISTENT_EVENT_ID = 2L;
    private final Long EXISTING_SECTOR_ID = 1L;
    private final Long EXISTING_SECTOR2_ID = 2L;


    @Before
    public void init() {
        Event existingEvent = new Event("Event 1", EventCategory.CULTURAL, "Event description");
        existingEvent.setId(EXISTING_EVENT_ID);

        when(eventRepository.findById(EXISTING_EVENT_ID)).
                thenReturn(Optional.of(existingEvent));


        LocationScheme locationScheme = new LocationScheme("Scheme 1", "Address 1");
        locationScheme.setId(EXISTING_LOCATION_ID);
        Location location = new Location(locationScheme);
        location.setId(EXISTING_LOCATION_ID);

        when(locationRepository.findById(EXISTING_LOCATION_ID)).
                thenReturn(Optional.of(location));


        EventDay eventDay = new EventDay(new Date(), location, new Date(), EventDayState.RESERVABLE_AND_BUYABLE, existingEvent);
        eventDay.setId(EXISTING_EVENT_DAY_ID);
        EventDay eventDay2 = new EventDay(new Date(), location, new Date(), EventDayState.RESERVABLE_AND_BUYABLE, existingEvent);
        eventDay2.setId(EXISTING_EVENT_DAY2_ID);

        when(eventDaysRepository.findAllByEventId(EXISTING_EVENT_ID)).
                thenReturn(Arrays.asList(eventDay, eventDay2));
        when(eventDaysRepository.findAllByLocationId(EXISTING_LOCATION_ID)).
                thenReturn(Arrays.asList(eventDay, eventDay2));


        User user = new User("user", "1234", "John", "Doe", "johnDoe@gmail.com");
        Ticket ticket = new Ticket(false, EXISTING_SECTOR_ID, 150.00, false, eventDay, user);
        Ticket ticket2 = new Ticket(true, EXISTING_SECTOR2_ID, 250.00, false, eventDay, user);
        Ticket ticket3 = new Ticket(true, EXISTING_SECTOR_ID, 150.00, false, eventDay2, user);
        Ticket ticket4 = new Ticket(true, EXISTING_SECTOR2_ID, 250.00, false, eventDay2, user);
        Ticket ticket5 = new Ticket(false, EXISTING_SECTOR2_ID, 250.00, false, eventDay2, user);

        when(ticketRepository.findByEventDayIdAndSoldTrueAndUserNotNull(EXISTING_EVENT_DAY_ID)).
                thenReturn(Arrays.asList(ticket2));
        when(ticketRepository.findByEventDayIdAndSoldFalseAndUserNotNull(EXISTING_EVENT_DAY_ID)).
                thenReturn(Arrays.asList(ticket));
        when(ticketRepository.findByEventDayIdAndSoldTrueAndUserNotNull(EXISTING_EVENT_DAY2_ID)).
                thenReturn(Arrays.asList(ticket3, ticket4));
        when(ticketRepository.findByEventDayIdAndSoldFalseAndUserNotNull(EXISTING_EVENT_DAY2_ID)).
                thenReturn(Arrays.asList(ticket5));


        Sector sector = new Sector(1.0, 1.0, 0.0, 0.0, 150, 0, 0, SectorType.PARTER, locationScheme);
        sector.setId(EXISTING_SECTOR_ID);
        Sector sector2 = new Sector(1.0, 1.0, 0.0, 0.0, 100, 10, 10, SectorType.GRANDSTAND, locationScheme);
        sector2.setId(EXISTING_SECTOR2_ID);
        LocationSector locationSector = new LocationSector(sector, location, 150.00, 150, false);
        locationSector.setId(EXISTING_SECTOR_ID);
        LocationSector locationSector2 = new LocationSector(sector2, location, 250.00, 100, false);
        locationSector2.setId(EXISTING_SECTOR2_ID);

        when(locationSectorRepository.findAllByLocationIdAndDeletedFalse(EXISTING_LOCATION_ID)).thenReturn(Arrays.asList(locationSector, locationSector2));
        reportsService = new ReportsServiceImpl(eventRepository, eventDaysRepository,
                ticketRepository, locationSectorRepository, locationRepository);
    }

    /**
     * Test report about event with id EXISTING_EVENT_ID == 1L
     *
     * @throws EventNotFound
     */
    @Test
    public void eventReport_Positive() throws EventNotFound {
        EventReportDTO eventReportDTO = reportsService.eventReport(EXISTING_EVENT_ID);
        assertEquals(3, eventReportDTO.getNumOfTickets());
        assertEquals(2, eventReportDTO.getNumOfReservations());
        assertEquals(650.0, eventReportDTO.getTotalIncome(), 0.001);
        assertEquals(216.666, eventReportDTO.getAvgPrice(), 0.001);
    }

    /**
     * Test report about event with NONEXISTENT_EVENT_ID == 2L fails
     *
     * @throws EventNotFound
     */
    @Test(expected = EventNotFound.class)
    public void eventReport_Negative_EventNotFound() throws EventNotFound {
        reportsService.eventReport(NONEXISTENT_EVENT_ID);
    }

    /**
     * Test report about event days of a single event with id EXISTING_EVENT_ID == 1L
     *
     * @throws EventNotFound
     */
    @Test
    public void eventDaysReport_Positive() throws EventNotFound {
        List<EventDayReportDTO> reports = reportsService.eventDaysReport(EXISTING_EVENT_ID);
        assertNotNull(reports);

        assertEquals(1, reports.get(0).getNumOfTickets());
        assertEquals(1, reports.get(0).getNumOfReservations());
        assertEquals(250.0, reports.get(0).getTotalIncome(), 0.001);
        assertEquals(250.0, reports.get(0).getAvgPrice(), 0.001);
        assertEquals(0, reports.get(0).getSoldBySector().get(EXISTING_SECTOR_ID), 0.001);

        assertEquals(2, reports.get(1).getNumOfTickets());
        assertEquals(1, reports.get(1).getNumOfReservations());
        assertEquals(400.0, reports.get(1).getTotalIncome(), 0.001);
        assertEquals(200.0, reports.get(1).getAvgPrice(), 0.001);
        assertEquals(1, reports.get(1).getSoldBySector().get(EXISTING_SECTOR2_ID), 0.001);
    }

    /**
     * Test report about event days of a single event with id NONEXISTENT_EVENT_ID == 2L fails
     *
     * @throws EventNotFound
     */
    @Test(expected = EventNotFound.class)
    public void eventDaysReport_Negative_EventNotFound() throws EventNotFound {
        reportsService.eventDaysReport(NONEXISTENT_EVENT_ID);
    }

    /**
     * Test report about events on a single location with id EXISTING_LOCATION_ID == 1L
     *
     * @throws LocationNotFound
     */
    @Test
    public void locationReport_Positive() throws LocationNotFound {
        LocationReportDTO locationReportDTO = reportsService.locationReport(EXISTING_LOCATION_ID);

        assertEquals(650.0, locationReportDTO.getTotalIncome(), 0.001);
        assertEquals(650.0, locationReportDTO.getIncomeByCategory().get(EventCategory.CULTURAL), 0.001);
        assertEquals(2, locationReportDTO.getNumOfEventDaysByCategory().get(EventCategory.CULTURAL), 0.001);

    }

    /**
     * Test report about location with id NONEXISTENT_LOCATION_ID == 2L fails
     *
     * @throws LocationNotFound
     */
    @Test(expected = LocationNotFound.class)
    public void locationReport_Negative_LocationNotFound() throws LocationNotFound {
        reportsService.locationReport(NONEXISTENT_LOCATION_ID);
    }
}