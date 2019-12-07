package nvt.kts.ticketapp.service.event.create;

import nvt.kts.ticketapp.domain.dto.event.*;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.exception.date.DateCantBeInThePast;
import nvt.kts.ticketapp.exception.date.DateFormatIsNotValid;
import nvt.kts.ticketapp.exception.event.EventDaysListEmpty;
import nvt.kts.ticketapp.exception.event.ReservationExpireDateInvalid;
import nvt.kts.ticketapp.exception.location.LocationNotAvailableThatDate;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.exception.sector.SectorCapacityOverload;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.service.event.EventService;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import nvt.kts.ticketapp.service.location.LocationService;
import nvt.kts.ticketapp.service.sector.SectorService;
import nvt.kts.ticketapp.util.DateUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateEventUnitTest{

    @Autowired
    private EventService eventService;

    @MockBean
    private LocationSchemeService locationSchemeServiceMocked;

    @MockBean
    private SectorService sectorServiceMocked;

    @MockBean
    private EventRepository eventRepositoryMocked;

    @MockBean
    private LocationRepository locationRepositoryMocked;

    @MockBean
    private LocationSectorRepository locationSectorRepositoryMocked;

    @MockBean
    private EventDaysRepository eventDaysRepositoryMocked;

    @MockBean
    private TicketRepository ticketRepositoryMocked;

    private static LocationScheme locationScheme;
    private static Sector sector1;
    private static Sector sector2;
    private static Location location;
    private static Location location2;

    @BeforeClass
    public static void setUp() {
        locationScheme = new LocationScheme("Spens", "Sutjeska 2, Novi Sad");
        locationScheme.setId(1L);

        sector1 = new Sector(0, 0, 0, 0, 5, 5, 1, SectorType.GRANDSTAND, locationScheme);
        sector1.setId(1L);
        sector2 = new Sector(0, 0, 0, 0, 5, 0, 0, SectorType.PARTER, locationScheme);
        sector2.setId(2L);

        location = new Location(locationScheme);
        location.setId(1L);

        location2 = new Location(locationScheme);
        location2.setId(2L);

    }

    private EventDTO generateEventDTO() {
        return new EventDTO(null, "opis", "Koncert Zdravka Colica", EventCategory.ENTERTAINMENT);
    }

    private EventEventDaysDTO generateOneEventDayDTO(EventDTO eventDTO) throws SectorDoesNotExist, LocationSchemeDoesNotExist {

        List<EventDayDTO> evenyDays = new ArrayList<>();

        List<LocationSectorsDTO> locationSectorsDTOS = new ArrayList<>();

        LocationSectorsDTO locationSectorsDTO1 = new LocationSectorsDTO(1L, 500, 5, false );
        LocationSectorsDTO locationSectorsDTO2 = new LocationSectorsDTO(2L, 300, 5, false );

        locationSectorsDTOS.add(locationSectorsDTO1);
        locationSectorsDTOS.add(locationSectorsDTO2);

        LocationDTO locationDTO = new LocationDTO(locationScheme.getId(), locationSectorsDTOS );

        EventDayDTO eventDayDTO = new EventDayDTO("2020-01-18", locationDTO, "2020-01-16");

        evenyDays.add(eventDayDTO);

        EventEventDaysDTO eventEventDaysDTO = new EventEventDaysDTO(eventDTO, evenyDays);

        Mockito.when(locationSchemeServiceMocked.getScheme(locationDTO.getLocationSchemeId())).thenReturn(locationScheme);
        Mockito.when(sectorServiceMocked.getSector(locationSectorsDTO1.getSectorId())).thenReturn(sector1);
        Mockito.when(sectorServiceMocked.getSector(locationSectorsDTO2.getSectorId())).thenReturn(sector2);
        Mockito.when(eventDaysRepositoryMocked.findAllByDate(Mockito.any(Date.class))).thenReturn(new ArrayList<>());

        Event event = new Event("Koncert Zdravka Colica", EventCategory.ENTERTAINMENT, "opis");
        Mockito.when(eventRepositoryMocked.save(Mockito.any(Event.class))).thenReturn(event);

        return eventEventDaysDTO;
    }

    private EventEventDaysDTO generateTwoEventDaysDTO(EventDTO eventDTO) throws LocationSchemeDoesNotExist, SectorDoesNotExist {

        List<EventDayDTO> eventDays = new ArrayList<>();


        // List of locationSectors for day 1
        List<LocationSectorsDTO> locationSectorsDTOS = new ArrayList<>();

        LocationSectorsDTO locationSectorsDTO1 = new LocationSectorsDTO(1L, 500, 5, false );
        LocationSectorsDTO locationSectorsDTO2 = new LocationSectorsDTO(2L, 300, 5, false );

        locationSectorsDTOS.add(locationSectorsDTO1);
        locationSectorsDTOS.add(locationSectorsDTO2);

        LocationDTO locationDTO = new LocationDTO(locationScheme.getId(), locationSectorsDTOS );


        // List of locationSectors for day 2
        List<LocationSectorsDTO> locationSectorsDTOS2 = new ArrayList<>();

        LocationSectorsDTO locationSectorsDTO2_1 = new LocationSectorsDTO(1L, 400, 5, false );
        LocationSectorsDTO locationSectorsDTO2_2 = new LocationSectorsDTO(2L, 200, 5, false );

        locationSectorsDTOS2.add(locationSectorsDTO2_1);
        locationSectorsDTOS2.add(locationSectorsDTO2_2);

        LocationDTO locationDTO2 =  new LocationDTO(locationScheme.getId(), locationSectorsDTOS2 );

        // EventDay 1
        EventDayDTO eventDayDTO = new EventDayDTO("2020-01-18", locationDTO, "2020-01-16");

        // EventDay 2
        EventDayDTO eventDayDTO2 = new EventDayDTO("2020-01-19", locationDTO2, "2020-01-17");

        eventDays.add(eventDayDTO);
        eventDays.add(eventDayDTO2);

        EventEventDaysDTO eventEventDaysDTO = new EventEventDaysDTO(eventDTO, eventDays);

        Mockito.when(locationSchemeServiceMocked.getScheme(locationDTO.getLocationSchemeId())).thenReturn(locationScheme);
        Mockito.when(sectorServiceMocked.getSector(locationSectorsDTO1.getSectorId())).thenReturn(sector1);
        Mockito.when(sectorServiceMocked.getSector(locationSectorsDTO2.getSectorId())).thenReturn(sector2);
        Mockito.when(eventDaysRepositoryMocked.findAllByDate(Mockito.any(Date.class))).thenReturn(new ArrayList<>());

        Event event = new Event("Koncert Zdravka Colica", EventCategory.ENTERTAINMENT, "opis");
        Mockito.when(eventRepositoryMocked.save(Mockito.any(Event.class))).thenReturn(event);

        return eventEventDaysDTO;
    }

    @Test
    public void save_oneEventDay_success() throws LocationSchemeDoesNotExist, SectorDoesNotExist, DateCantBeInThePast, LocationNotAvailableThatDate, SectorCapacityOverload, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, ParseException {

        EventDTO eventDTO = generateEventDTO();
        EventEventDaysDTO eventEventDaysDTO = generateOneEventDayDTO(eventDTO);

        Event createdEvent = eventService.create(eventEventDaysDTO);

        assertNotNull(createdEvent);
        assertEquals(createdEvent.getName(), eventDTO.getName());
        assertEquals(createdEvent.getCategory(), eventDTO.getCategory());
        assertEquals(createdEvent.getDescription(), eventDTO.getDescription());

        Mockito.verify(locationSchemeServiceMocked).getScheme(locationScheme.getId());
        Mockito.verify(sectorServiceMocked).getSector(sector1.getId());
        Mockito.verify(sectorServiceMocked).getSector(sector2.getId());
        Mockito.verify(eventRepositoryMocked).save(Mockito.any(Event.class));
        Mockito.verify(locationRepositoryMocked).saveAll(Mockito.anyList());
        Mockito.verify(locationSectorRepositoryMocked).saveAll(Mockito.anyList());
        Mockito.verify(eventDaysRepositoryMocked).saveAll(Mockito.anyList());
        Mockito.verify(ticketRepositoryMocked).saveAll((Mockito.anyList()));

    }

    @Test
    public void save_twoEventDays_success() throws LocationSchemeDoesNotExist, SectorDoesNotExist, DateCantBeInThePast, LocationNotAvailableThatDate, SectorCapacityOverload, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, ParseException {

        EventDTO eventDTO = generateEventDTO();
        EventEventDaysDTO eventEventDaysDTO = generateTwoEventDaysDTO(eventDTO);

        Event createdEvent = eventService.create(eventEventDaysDTO);

        assertNotNull(createdEvent);
        assertEquals(createdEvent.getName(), eventDTO.getName());
        assertEquals(createdEvent.getCategory(), eventDTO.getCategory());
        assertEquals(createdEvent.getDescription(), eventDTO.getDescription());

        Mockito.verify(locationSchemeServiceMocked, Mockito.times(2)).getScheme(locationScheme.getId());
        Mockito.verify(sectorServiceMocked, Mockito.times(2)).getSector(sector1.getId());
        Mockito.verify(sectorServiceMocked, Mockito.times(2)).getSector(sector2.getId());
        Mockito.verify(eventRepositoryMocked).save(Mockito.any(Event.class));
        Mockito.verify(locationRepositoryMocked).saveAll(Mockito.anyList());
        Mockito.verify(locationSectorRepositoryMocked).saveAll(Mockito.anyList());
        Mockito.verify(eventDaysRepositoryMocked).saveAll(Mockito.anyList());
        Mockito.verify(ticketRepositoryMocked).saveAll((Mockito.anyList()));

    }

    @Test(expected = LocationSchemeDoesNotExist.class)
    public void save_oneEventDay_LocationSchemeDoesNotExist() throws LocationSchemeDoesNotExist, SectorDoesNotExist, SectorCapacityOverload, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, LocationNotAvailableThatDate, DateCantBeInThePast, ParseException {

        EventDTO eventDTO = generateEventDTO();
        EventEventDaysDTO eventEventDaysDTO = generateOneEventDayDTO(eventDTO);

        Long locationSchemeId = 2L;

        eventEventDaysDTO.getEventDays().get(0).getLocation().setLocationSchemeId(locationSchemeId);
        Mockito.when(locationSchemeServiceMocked.getScheme(locationSchemeId)).thenThrow(new LocationSchemeDoesNotExist(locationSchemeId));

        try {
            Event createdEvent = eventService.create(eventEventDaysDTO);
        } catch (DateFormatIsNotValid | LocationSchemeDoesNotExist | SectorDoesNotExist | LocationNotAvailableThatDate | EventDaysListEmpty | SectorCapacityOverload | DateCantBeInThePast | ReservationExpireDateInvalid | ParseException ex) {
            ex.printStackTrace();

            Mockito.verify(locationSchemeServiceMocked).getScheme(locationSchemeId);
            Mockito.verify(sectorServiceMocked, Mockito.times(0)).getSector(sector1.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(0)).getSector(sector2.getId());
            Mockito.verify(eventRepositoryMocked, Mockito.times(0)).save(Mockito.any(Event.class));
            Mockito.verify(locationRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(locationSectorRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(eventDaysRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(ticketRepositoryMocked, Mockito.times(0)).saveAll((Mockito.anyList()));

            throw ex;
        }
    }

    @Test(expected = LocationSchemeDoesNotExist.class)
    public void save_twoEventDays_LocationSchemeDoesNotExist() throws LocationSchemeDoesNotExist, SectorDoesNotExist, SectorCapacityOverload, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, LocationNotAvailableThatDate, DateCantBeInThePast, ParseException {

        EventDTO eventDTO = generateEventDTO();
        EventEventDaysDTO eventEventDaysDTO = generateTwoEventDaysDTO(eventDTO);

        Long locationSchemeId = 2L;

        eventEventDaysDTO.getEventDays().get(1).getLocation().setLocationSchemeId(locationSchemeId);
        Mockito.when(locationSchemeServiceMocked.getScheme(locationScheme.getId())).thenReturn(locationScheme);
        Mockito.when(locationSchemeServiceMocked.getScheme(locationSchemeId)).thenThrow(new LocationSchemeDoesNotExist(locationSchemeId));

        try {
            Event createdEvent = eventService.create(eventEventDaysDTO);
        } catch (DateFormatIsNotValid | LocationSchemeDoesNotExist | SectorDoesNotExist | LocationNotAvailableThatDate | EventDaysListEmpty | SectorCapacityOverload | DateCantBeInThePast | ReservationExpireDateInvalid | ParseException ex) {
            ex.printStackTrace();

            Mockito.verify(locationSchemeServiceMocked, Mockito.times(1)).getScheme(locationScheme.getId());
            Mockito.verify(locationSchemeServiceMocked, Mockito.times(1)).getScheme(locationSchemeId);
            Mockito.verify(sectorServiceMocked, Mockito.times(1)).getSector(sector1.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(1)).getSector(sector2.getId());
            Mockito.verify(eventRepositoryMocked, Mockito.times(0)).save(Mockito.any(Event.class));
            Mockito.verify(locationRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(locationSectorRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(eventDaysRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(ticketRepositoryMocked, Mockito.times(0)).saveAll((Mockito.anyList()));

            throw ex;
        }
    }

    @Test(expected = SectorDoesNotExist.class)
    public void save_oneEventDay_SectorDoesNotExist() throws LocationSchemeDoesNotExist, SectorDoesNotExist, DateCantBeInThePast, LocationNotAvailableThatDate, SectorCapacityOverload, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, ParseException {

        EventDTO eventDTO = generateEventDTO();
        EventEventDaysDTO eventEventDaysDTO = generateOneEventDayDTO(eventDTO);

        Long sectorId = 4L;

        eventEventDaysDTO.getEventDays().get(0).getLocation().getLocationSectors().get(0).setSectorId(sectorId);
        Mockito.when(sectorServiceMocked.getSector(sectorId)).thenThrow(new SectorDoesNotExist());

        try {
            Event createdEvent = eventService.create(eventEventDaysDTO);
        } catch (DateFormatIsNotValid | LocationSchemeDoesNotExist | SectorDoesNotExist | LocationNotAvailableThatDate | EventDaysListEmpty | SectorCapacityOverload | DateCantBeInThePast | ReservationExpireDateInvalid | ParseException ex) {
            ex.printStackTrace();

            Mockito.verify(locationSchemeServiceMocked).getScheme(locationScheme.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(1)).getSector(sectorId);
            Mockito.verify(sectorServiceMocked, Mockito.times(0)).getSector(sector2.getId());
            Mockito.verify(eventRepositoryMocked, Mockito.times(0)).save(Mockito.any(Event.class));
            Mockito.verify(locationRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(locationSectorRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(eventDaysRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(ticketRepositoryMocked, Mockito.times(0)).saveAll((Mockito.anyList()));

            throw ex;
        }
    }

    @Test(expected = SectorDoesNotExist.class)
    public void save_oneEventDay_SectorDoesNotExist_2() throws LocationSchemeDoesNotExist, SectorDoesNotExist, DateCantBeInThePast, LocationNotAvailableThatDate, SectorCapacityOverload, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, ParseException {

        EventDTO eventDTO = generateEventDTO();
        EventEventDaysDTO eventEventDaysDTO = generateOneEventDayDTO(eventDTO);

        Long sectorId = 4L;

        eventEventDaysDTO.getEventDays().get(0).getLocation().getLocationSectors().get(1).setSectorId(sectorId);
        Mockito.when(sectorServiceMocked.getSector(sectorId)).thenThrow(new SectorDoesNotExist());

        try {
            Event createdEvent = eventService.create(eventEventDaysDTO);
        } catch (DateFormatIsNotValid | LocationSchemeDoesNotExist | SectorDoesNotExist | LocationNotAvailableThatDate | EventDaysListEmpty | SectorCapacityOverload | DateCantBeInThePast | ReservationExpireDateInvalid | ParseException ex) {
            ex.printStackTrace();

            Mockito.verify(locationSchemeServiceMocked).getScheme(locationScheme.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(1)).getSector(sector1.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(1)).getSector(sectorId);
            Mockito.verify(eventRepositoryMocked, Mockito.times(0)).save(Mockito.any(Event.class));
            Mockito.verify(locationRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(locationSectorRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(eventDaysRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(ticketRepositoryMocked, Mockito.times(0)).saveAll((Mockito.anyList()));

            throw ex;
        }
    }

    @Test(expected = DateCantBeInThePast.class)
    public void save_oneEventDay_DateCantBeInThePast() throws LocationSchemeDoesNotExist, SectorDoesNotExist, DateCantBeInThePast, LocationNotAvailableThatDate, SectorCapacityOverload, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, ParseException {

        EventDTO eventDTO = generateEventDTO();
        EventEventDaysDTO eventEventDaysDTO = generateOneEventDayDTO(eventDTO);

        eventEventDaysDTO.getEventDays().get(0).setDate("2018-11-20");

        try {
            Event createdEvent = eventService.create(eventEventDaysDTO);
        } catch (DateFormatIsNotValid | LocationSchemeDoesNotExist | SectorDoesNotExist | LocationNotAvailableThatDate | EventDaysListEmpty | SectorCapacityOverload | DateCantBeInThePast | ReservationExpireDateInvalid | ParseException ex) {
            ex.printStackTrace();

            Mockito.verify(locationSchemeServiceMocked, Mockito.times(0)).getScheme(locationScheme.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(0)).getSector(sector1.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(0)).getSector(sector2.getId());
            Mockito.verify(eventRepositoryMocked, Mockito.times(0)).save(Mockito.any(Event.class));
            Mockito.verify(locationRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(locationSectorRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(eventDaysRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(ticketRepositoryMocked, Mockito.times(0)).saveAll((Mockito.anyList()));

            throw ex;
        }
    }

    @Test(expected = LocationNotAvailableThatDate.class)
    public void save_oneEventDay_LocationNotAvailableThatDate() throws LocationSchemeDoesNotExist, SectorDoesNotExist, DateCantBeInThePast, LocationNotAvailableThatDate, SectorCapacityOverload, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, ParseException {

        EventDTO eventDTO = generateEventDTO();
        EventEventDaysDTO eventEventDaysDTO = generateOneEventDayDTO(eventDTO);

        List<EventDay> eventDays = new ArrayList<>();
        EventDay eventDay = new EventDay();
        eventDay.setDate(DateUtil.parseDate("2020-01-18", "yyyy-MM-dd"));
        eventDay.setLocation(location);
        eventDays.add(eventDay);

        Mockito.when(eventDaysRepositoryMocked.findAllByLocationId(location.getId())).thenReturn(eventDays);

        try {
            Event createdEvent = eventService.create(eventEventDaysDTO);
        } catch (DateFormatIsNotValid | LocationSchemeDoesNotExist | SectorDoesNotExist | LocationNotAvailableThatDate | EventDaysListEmpty | SectorCapacityOverload | DateCantBeInThePast | ReservationExpireDateInvalid | ParseException ex) {
            ex.printStackTrace();

            Mockito.verify(locationSchemeServiceMocked).getScheme(locationScheme.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(0)).getSector(sector1.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(0)).getSector(sector2.getId());
            Mockito.verify(eventRepositoryMocked, Mockito.times(0)).save(Mockito.any(Event.class));
            Mockito.verify(locationRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(locationSectorRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(eventDaysRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(ticketRepositoryMocked, Mockito.times(0)).saveAll((Mockito.anyList()));

            throw ex;
        }
    }

    @Test(expected = SectorCapacityOverload.class)
    public void save_oneEventDay_SectorCapacityOverload() throws LocationSchemeDoesNotExist, SectorDoesNotExist, DateCantBeInThePast, LocationNotAvailableThatDate, SectorCapacityOverload, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, ParseException {

        EventDTO eventDTO = generateEventDTO();
        EventEventDaysDTO eventEventDaysDTO = generateOneEventDayDTO(eventDTO);

        eventEventDaysDTO.getEventDays().get(0).getLocation().getLocationSectors().get(0).setCapacity(6);

        try {
            Event createdEvent = eventService.create(eventEventDaysDTO);
        } catch (DateFormatIsNotValid | LocationSchemeDoesNotExist | SectorDoesNotExist | LocationNotAvailableThatDate | EventDaysListEmpty | SectorCapacityOverload | DateCantBeInThePast | ReservationExpireDateInvalid | ParseException ex) {
            ex.printStackTrace();

            Mockito.verify(locationSchemeServiceMocked,Mockito.times(1)).getScheme(locationScheme.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(1)).getSector(sector1.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(0)).getSector(sector2.getId());
            Mockito.verify(eventRepositoryMocked, Mockito.times(0)).save(Mockito.any(Event.class));
            Mockito.verify(locationRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(locationSectorRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(eventDaysRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(ticketRepositoryMocked, Mockito.times(0)).saveAll((Mockito.anyList()));

            throw ex;
        }
    }

    @Test(expected = ReservationExpireDateInvalid.class)
    public void save_oneEventDay_ReservationExpireDateInvalid() throws LocationSchemeDoesNotExist, SectorDoesNotExist, ReservationExpireDateInvalid, LocationNotAvailableThatDate, EventDaysListEmpty, DateFormatIsNotValid, SectorCapacityOverload, DateCantBeInThePast, ParseException {

        EventDTO eventDTO = generateEventDTO();
        EventEventDaysDTO eventEventDaysDTO = generateOneEventDayDTO(eventDTO);

        eventEventDaysDTO.getEventDays().get(0).setReservationExpireDate("2020-01-19");

        try {
            Event createdEvent = eventService.create(eventEventDaysDTO);
        } catch (DateFormatIsNotValid | LocationSchemeDoesNotExist | SectorDoesNotExist | LocationNotAvailableThatDate | EventDaysListEmpty | SectorCapacityOverload | DateCantBeInThePast | ReservationExpireDateInvalid | ParseException ex) {
            ex.printStackTrace();
            Mockito.verify(locationSchemeServiceMocked,Mockito.times(0)).getScheme(locationScheme.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(0)).getSector(sector1.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(0)).getSector(sector2.getId());
            Mockito.verify(eventRepositoryMocked, Mockito.times(0)).save(Mockito.any(Event.class));
            Mockito.verify(locationRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(locationSectorRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(eventDaysRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(ticketRepositoryMocked, Mockito.times(0)).saveAll((Mockito.anyList()));

            throw ex;
        }
    }

    @Test(expected = DateFormatIsNotValid.class)
    public void save_oneEventDay_DateFormatIsNotValid() throws LocationSchemeDoesNotExist, SectorDoesNotExist, DateCantBeInThePast, LocationNotAvailableThatDate, SectorCapacityOverload, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, ParseException {

        EventDTO eventDTO = generateEventDTO();
        EventEventDaysDTO eventEventDaysDTO = generateOneEventDayDTO(eventDTO);

        eventEventDaysDTO.getEventDays().get(0).setDate("20-11-2021");

        try {
            Event createdEvent = eventService.create(eventEventDaysDTO);
        } catch (DateFormatIsNotValid | LocationSchemeDoesNotExist | SectorDoesNotExist | LocationNotAvailableThatDate | EventDaysListEmpty | SectorCapacityOverload | DateCantBeInThePast | ReservationExpireDateInvalid | ParseException ex) {
            ex.printStackTrace();

            Mockito.verify(locationSchemeServiceMocked,Mockito.times(0)).getScheme(locationScheme.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(0)).getSector(sector1.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(0)).getSector(sector2.getId());
            Mockito.verify(eventRepositoryMocked, Mockito.times(0)).save(Mockito.any(Event.class));
            Mockito.verify(locationRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(locationSectorRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(eventDaysRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(ticketRepositoryMocked, Mockito.times(0)).saveAll((Mockito.anyList()));

            throw ex;
        }
    }

    @Test(expected = EventDaysListEmpty.class)
    public void save_oneEventDay_EventDaysListEmpty() throws LocationSchemeDoesNotExist, SectorDoesNotExist, DateCantBeInThePast, LocationNotAvailableThatDate, SectorCapacityOverload, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, ParseException {

        EventDTO eventDTO = generateEventDTO();
        EventEventDaysDTO eventEventDaysDTO = generateOneEventDayDTO(eventDTO);

        eventEventDaysDTO.setEventDays(new ArrayList<>());

        try {
            Event createdEvent = eventService.create(eventEventDaysDTO);
        } catch (DateFormatIsNotValid | LocationSchemeDoesNotExist | SectorDoesNotExist | LocationNotAvailableThatDate | EventDaysListEmpty | SectorCapacityOverload | DateCantBeInThePast | ReservationExpireDateInvalid | ParseException ex) {
            ex.printStackTrace();

            Mockito.verify(locationSchemeServiceMocked,Mockito.times(0)).getScheme(locationScheme.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(0)).getSector(sector1.getId());
            Mockito.verify(sectorServiceMocked, Mockito.times(0)).getSector(sector2.getId());
            Mockito.verify(eventRepositoryMocked, Mockito.times(0)).save(Mockito.any(Event.class));
            Mockito.verify(locationRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(locationSectorRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(eventDaysRepositoryMocked, Mockito.times(0)).saveAll(Mockito.anyList());
            Mockito.verify(ticketRepositoryMocked, Mockito.times(0)).saveAll((Mockito.anyList()));

            throw ex;
        }
    }
}
