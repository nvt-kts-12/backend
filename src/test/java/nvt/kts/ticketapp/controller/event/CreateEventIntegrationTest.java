package nvt.kts.ticketapp.controller.event;

import nvt.kts.ticketapp.ClearDatabaseRule;
import nvt.kts.ticketapp.domain.dto.event.*;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.exception.date.DateFormatIsNotValid;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.repository.user.AdminRepository;
import nvt.kts.ticketapp.repository.user.AuthorityRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import nvt.kts.ticketapp.service.event.EventService;
import nvt.kts.ticketapp.util.DateUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test, test-conf")
public class CreateEventIntegrationTest {

    @Autowired
    private TestRestTemplate adminTemplate;

    @Rule
    @Autowired
    public ClearDatabaseRule clearDatabaseRule;

    @Autowired
    private EventService eventService;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventDaysRepository eventDaysRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationSectorRepository locationSectorRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private LocationSchemeRepository locationSchemeRepository;
    @Autowired
    private SectorRepository sectorRepository;

    private LocationScheme locationScheme;
    private Sector sector1;
    private Sector sector2;

    private EventEventDaysDTO eventEventDaysDTO;

    private String url = "/api/event";

    @PostConstruct
    public void postConstruct() {
        this.adminTemplate = this.adminTemplate.withBasicAuth("admin", "password");
    }

    @Before
    public void createEventEventDaysDTO() {

        EventDTO eventDTO = generateEventDTO();
        eventEventDaysDTO = generateOneEventDayDTO(eventDTO);
    }

    @Before
    public void initLocations() {
        locationScheme = new LocationScheme("Spens", "Sutjeska 2, Novi Sad");

        locationSchemeRepository.save(locationScheme);

        sector1 = new Sector(0, 0, 0, 0, 5, 5, 1, SectorType.GRANDSTAND, locationScheme);
        sector2 = new Sector(0, 0, 0, 0, 5, 0, 0, SectorType.PARTER, locationScheme);

        sectorRepository.save(sector1);
        sectorRepository.save(sector2);

    }

    private EventDTO generateEventDTO() {
        return new EventDTO(null, "opis", "Koncert Zdravka Colica", EventCategory.ENTERTAINMENT);
    }

    private EventEventDaysDTO generateOneEventDayDTO(EventDTO eventDTO){

        List<EventDayDTO> evenyDays = new ArrayList<>();

        List<LocationSectorsDTO> locationSectorsDTOS = new ArrayList<>();

        LocationSectorsDTO locationSectorsDTO1 = new LocationSectorsDTO(1L, 500, 5, false );
        LocationSectorsDTO locationSectorsDTO2 = new LocationSectorsDTO(2L, 300, 5, false );

        locationSectorsDTOS.add(locationSectorsDTO1);
        locationSectorsDTOS.add(locationSectorsDTO2);

        LocationDTO locationDTO = new LocationDTO(locationScheme.getId(), locationSectorsDTOS );

        EventDayDTO eventDayDTO = new EventDayDTO("2020-10-18", locationDTO, "2020-10-16");

        evenyDays.add(eventDayDTO);

        return new EventEventDaysDTO(eventDTO, evenyDays);
    }

    @Test
    public void create_success() {

        ResponseEntity<EventDTO> response = adminTemplate.postForEntity(url, new HttpEntity<>(eventEventDaysDTO), EventDTO.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void create_DateFormatIsNotValid() {

        eventEventDaysDTO.getEventDays().get(0).setDate("05-10-2020");
        ResponseEntity<String> response = adminTemplate.postForEntity(url, new HttpEntity<>(eventEventDaysDTO), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Date format is not valid", response.getBody());
    }

    @Test
    public void create_LocationSchemeDoesNotExist() {

        eventEventDaysDTO.getEventDays().get(0).getLocation().setLocationSchemeId(10L);
        ResponseEntity<String> response = adminTemplate.postForEntity(url, new HttpEntity<>(eventEventDaysDTO), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Location scheme with id: 10 doesn't exist", response.getBody());
    }

    @Test
    public void create_SectorDoesNotExist() {

        eventEventDaysDTO.getEventDays().get(0).getLocation().getLocationSectors().get(0).setSectorId(10L);
        ResponseEntity<String> response = adminTemplate.postForEntity(url, new HttpEntity<>(eventEventDaysDTO), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Sector doesn't exist", response.getBody());
    }


    @Test
    public void create_LocationNotAvailableThatDate() throws DateFormatIsNotValid {

        Location location = new Location(locationScheme);
        locationRepository.save(location);

        LocationSector locationSector = new LocationSector(sector1, location, 200, 1, false);
        locationSectorRepository.save(locationSector);

        Event event = new Event("Event", EventCategory.ENTERTAINMENT, "desc");

        Date date = DateUtil.parseDate("2020-10-18", "yyyy-MM-dd");
        Date reservationExpirationDate = DateUtil.parseDate("2020-10-16", "yyyy-MM-dd");

        EventDay eventDay = new EventDay(date, location, reservationExpirationDate, EventDayState.NOT_IN_SALE, event);

        eventRepository.save(event);
        eventDaysRepository.save(eventDay);

        ResponseEntity<String> response = adminTemplate.postForEntity(url, new HttpEntity<>(eventEventDaysDTO), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Location is not available for that date", response.getBody());
    }

    @Test
    public void create_EventDaysListEmpty() {

        eventEventDaysDTO.setEventDays(new ArrayList<>());
        ResponseEntity<String> response = adminTemplate.postForEntity(url, new HttpEntity<>(eventEventDaysDTO), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Event days list can't be empty", response.getBody());
    }

    @Test
    public void create_SectorCapacityOverload() {

        eventEventDaysDTO.getEventDays().get(0).getLocation().getLocationSectors().get(0).setCapacity(100);
        ResponseEntity<String> response = adminTemplate.postForEntity(url, new HttpEntity<>(eventEventDaysDTO), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Sector capacity overload", response.getBody());
    }

    @Test
    public void create_DateCantBeInThePast() {

        eventEventDaysDTO.getEventDays().get(0).setDate("2019-05-05");
        ResponseEntity<String> response = adminTemplate.postForEntity(url, new HttpEntity<>(eventEventDaysDTO), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Date can't be in the past", response.getBody());
    }

    @Test
    public void create_ReservationExpireDateInvalid() {

        eventEventDaysDTO.getEventDays().get(0).setReservationExpireDate("2021-05-05");
        ResponseEntity<String> response = adminTemplate.postForEntity(url, new HttpEntity<>(eventEventDaysDTO), String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Reservation expire date must be before event date", response.getBody());
    }
}
