package nvt.kts.ticketapp.controller.report;

import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.PlainEventDayDTO;
import nvt.kts.ticketapp.domain.dto.location.PlainLocationDTO;
import nvt.kts.ticketapp.domain.dto.report.EventDayReportDTO;
import nvt.kts.ticketapp.domain.dto.report.EventReportDTO;
import nvt.kts.ticketapp.domain.dto.report.LocationReportDTO;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.exception.location.LocationNotFound;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.service.report.ReportsService;
import nvt.kts.ticketapp.service.report.ReportsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportsControllerTest {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private ReportsServiceImpl reportsService;

    private static final String URL_PREFIX = "/api/reports";

    private final Long EXISTING_EVENT_ID = 1L;
    private final Long EXISTING_SCHEME_ID = 1L;
    private final Long EXISTING_LOCATION_ID = 1L;
    private final Long EXISTING_EVENT_DAY_ID = 1L;
    private final Long EXISTING_EVENT_DAY_ID_2 = 2L;

    @Before
    public void setUp() throws Exception {
        PlainLocationDTO plainLocationDTO = new PlainLocationDTO(EXISTING_SCHEME_ID);
        LocationReportDTO locationReportDTO = new LocationReportDTO(plainLocationDTO, 125, new HashMap<>(), new HashMap<>());

        EventDTO eventDTO = new EventDTO(EXISTING_EVENT_ID, "Event", "Football", EventCategory.SPORT);
        EventReportDTO eventReportDTO = new EventReportDTO(eventDTO, 5, 5, 125, 25);


        Map<Long, Integer> soldBySector = new HashMap<>();
        soldBySector.put(1L, 1);
        soldBySector.put(2L, 2);
        soldBySector.put(3L, 2);
        PlainEventDayDTO eventDayDTO = new PlainEventDayDTO(EXISTING_EVENT_DAY_ID, "2019-05-07", plainLocationDTO, "2019-05-05");
        EventDayReportDTO eventDayReportDTO = new EventDayReportDTO(eventDayDTO, 5, 5,
                125.0, 25.0, soldBySector);


        Map<Long, Integer> soldBySector2 = new HashMap<>();
        soldBySector2.put(1L, 1);
        soldBySector2.put(2L, 2);
        soldBySector2.put(3L, 2);
        PlainEventDayDTO eventDayDTO2 = new PlainEventDayDTO(EXISTING_EVENT_DAY_ID_2, "2019-05-08", plainLocationDTO, "2019-05-05");
        EventDayReportDTO eventDayReportDTO2 = new EventDayReportDTO(eventDayDTO2, 5, 5,
                125.0, 25.0, soldBySector2);


        when(reportsService.locationReport(anyLong())).thenReturn(locationReportDTO);
        when(reportsService.eventReport(anyLong())).thenReturn(eventReportDTO);
        when(reportsService.eventDaysReport(anyLong())).thenReturn(Arrays.asList(eventDayReportDTO, eventDayReportDTO2));
    }

    /**
     * Test report about event with id 1L
     *
     * @throws EventNotFound
     */
    @Test
    public void eventReport_Positive() throws EventNotFound {
        ResponseEntity<EventReportDTO> response = testRestTemplate.getForEntity(URL_PREFIX + "/event/1", EventReportDTO.class);

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getEventDTO());
        assertEquals(EXISTING_EVENT_ID, response.getBody().getEventDTO().getId());
        assertEquals(5, response.getBody().getNumOfTickets());

        verify(reportsService, times(1)).eventReport(anyLong());
    }

    /**
     * Test report about event with id 2L fails because there is no event with such id
     *
     * @throws EventNotFound
     */
    @Test
    public void eventReport_Negative_EventNotFound() throws EventNotFound {
        when(reportsService.eventReport(anyLong())).thenThrow(EventNotFound.class);
        ResponseEntity<EventReportDTO> response = testRestTemplate.getForEntity(URL_PREFIX + "/event/2", EventReportDTO.class);

        assertNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    /**
     * Test report about every day of an single event with id 1L
     *
     * @throws EventNotFound
     */
    @Test
    public void eventDaysReport() throws EventNotFound {
        ResponseEntity<EventDayReportDTO[]> response = testRestTemplate.getForEntity
                (URL_PREFIX + "/eventDay/1", EventDayReportDTO[].class);

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
        assertEquals(EXISTING_EVENT_DAY_ID, response.getBody()[0].getEventDayDTO().getId());
        assertEquals(EXISTING_EVENT_DAY_ID_2, response.getBody()[1].getEventDayDTO().getId());

        verify(reportsService, times(1)).eventDaysReport(anyLong());
    }

    /**
     * Test report about every day of an single event fails because event with id 2L does not exist
     *
     * @throws EventNotFound
     */
    @Test
    public void eventDaysReport_Negative_EventNotFound() throws EventNotFound {
        when(reportsService.eventDaysReport(anyLong())).thenThrow(EventNotFound.class);
        ResponseEntity<EventDayReportDTO[]> response = testRestTemplate.getForEntity
                (URL_PREFIX + "/eventDay/2", EventDayReportDTO[].class);

        assertNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test report about events on single location with id 1L
     *
     * @throws LocationNotFound
     */
    @Test
    public void locationReport_Positive() throws LocationNotFound {
        ResponseEntity<LocationReportDTO> response = testRestTemplate.getForEntity(URL_PREFIX + "/location/1", LocationReportDTO.class);

        assertNotNull(response);
        assertEquals(EXISTING_SCHEME_ID, response.getBody().getLocationDTO().getLocationSchemeId());

        verify(reportsService, times(1)).locationReport(anyLong());
    }

    /**
     * Test report about events on single location fails because location with id 2L does not exist
     *
     * @throws LocationNotFound
     */
    @Test
    public void locationReport_Negative_LocationNotFound() throws LocationNotFound {
        when(reportsService.locationReport(anyLong())).thenThrow(LocationNotFound.class);
        ResponseEntity<LocationReportDTO> response = testRestTemplate.getForEntity
                (URL_PREFIX + "/location/2", LocationReportDTO.class);

        assertNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}