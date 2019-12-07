package nvt.kts.ticketapp.service.event.create;

import nvt.kts.ticketapp.ClearDatabaseRule;
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
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.service.event.EventService;
import nvt.kts.ticketapp.util.DateUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateEventIntegrationTest {

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
    private  LocationSchemeRepository locationSchemeRepository;
    @Autowired
    private SectorRepository sectorRepository;

    private LocationScheme locationScheme;
    private Sector sector1;
    private Sector sector2;

    private EventEventDaysDTO eventEventDaysDTO;

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

        EventDayDTO eventDayDTO = new EventDayDTO("2020-03-18", locationDTO, "2020-03-16");

        evenyDays.add(eventDayDTO);

        return new EventEventDaysDTO(eventDTO, evenyDays);
    }

    @Test
    public void create_oneEventDay_success() throws LocationNotAvailableThatDate, DateCantBeInThePast, SectorCapacityOverload, LocationSchemeDoesNotExist, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, SectorDoesNotExist, ParseException {

        Event createdEvent = eventService.create(eventEventDaysDTO);
        EventDTO eventDTO = eventEventDaysDTO.getEvent();

        assertNotNull(createdEvent);
        assertEquals(createdEvent.getName(), eventDTO.getName());
        assertEquals(createdEvent.getCategory(), eventDTO.getCategory());
        assertEquals(createdEvent.getDescription(), eventDTO.getDescription());


        int numberOfTicketsToGenerate = 0;
        for(LocationSectorsDTO locationSectorsDTO : eventEventDaysDTO.getEventDays().get(0).getLocation().getLocationSectors()) {
            numberOfTicketsToGenerate += locationSectorsDTO.getCapacity();
        }

        assertEquals(1, eventRepository.findAll().size());
        assertEquals(numberOfTicketsToGenerate, ticketRepository.findAll().size());
        assertEquals(1, eventDaysRepository.findAll().size());
        assertEquals(1, locationRepository.findAll().size());
        assertEquals(2, locationSectorRepository.findAll().size());

    }

    @Test(expected = LocationNotAvailableThatDate.class)
    public void create_oneEventDay_LocationNotAvailableThatDate() throws LocationNotAvailableThatDate, DateCantBeInThePast, SectorCapacityOverload, LocationSchemeDoesNotExist, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, SectorDoesNotExist, ParseException {

        Location location = new Location(locationScheme);
        locationRepository.save(location);

        LocationSector locationSector = new LocationSector(sector1, location, 200, 1, false);
        locationSectorRepository.save(locationSector);

        Event event = new Event("Event", EventCategory.ENTERTAINMENT, "desc");

        Date date = DateUtil.parseDate("2020-03-18", "yyyy-MM-dd");
        Date reservationExpirationDate = DateUtil.parseDate("2020-03-16", "yyyy-MM-dd");

        EventDay eventDay = new EventDay(date, location, reservationExpirationDate, EventDayState.NOT_IN_SALE, event);

        eventRepository.save(event);
        eventDaysRepository.save(eventDay);


        Event createdEvent = eventService.create(eventEventDaysDTO);

        assertEquals(0, eventRepository.findAll().size());
        assertEquals(0, ticketRepository.findAll().size());
        assertEquals(0, eventDaysRepository.findAll().size());
        assertEquals(0, locationRepository.findAll().size());
        assertEquals(0, locationSectorRepository.findAll().size());

    }

    @Test(expected = DateCantBeInThePast.class)
    public void create_oneEventDay_DateCantBeInThePast() throws LocationNotAvailableThatDate, DateCantBeInThePast, SectorCapacityOverload, LocationSchemeDoesNotExist, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, SectorDoesNotExist, ParseException {

        eventEventDaysDTO.getEventDays().get(0).setDate("2019-05-05");
        Event createdEvent = eventService.create(eventEventDaysDTO);

        assertEquals(0, eventRepository.findAll().size());
        assertEquals(0, ticketRepository.findAll().size());
        assertEquals(0, eventDaysRepository.findAll().size());
        assertEquals(0, locationRepository.findAll().size());
        assertEquals(0, locationSectorRepository.findAll().size());
    }

    @Test(expected = SectorCapacityOverload.class)
    public void create_oneEventDay_SectorCapacityOverload() throws LocationNotAvailableThatDate, DateCantBeInThePast, SectorCapacityOverload, LocationSchemeDoesNotExist, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, SectorDoesNotExist, ParseException {

        eventEventDaysDTO.getEventDays().get(0).getLocation().getLocationSectors().get(0).setCapacity(10);
        Event createdEvent = eventService.create(eventEventDaysDTO);

        assertEquals(0, eventRepository.findAll().size());
        assertEquals(0, ticketRepository.findAll().size());
        assertEquals(0, eventDaysRepository.findAll().size());
        assertEquals(0, locationRepository.findAll().size());
        assertEquals(0, locationSectorRepository.findAll().size());
    }

    @Test(expected = LocationSchemeDoesNotExist.class)
    public void create_oneEventDay_LocationSchemeDoesNotExist() throws LocationNotAvailableThatDate, DateCantBeInThePast, SectorCapacityOverload, LocationSchemeDoesNotExist, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, SectorDoesNotExist, ParseException {

        eventEventDaysDTO.getEventDays().get(0).getLocation().setLocationSchemeId(10L);
        Event createdEvent = eventService.create(eventEventDaysDTO);

        assertEquals(0, eventRepository.findAll().size());
        assertEquals(0, ticketRepository.findAll().size());
        assertEquals(0, eventDaysRepository.findAll().size());
        assertEquals(0, locationRepository.findAll().size());
        assertEquals(0, locationSectorRepository.findAll().size());
    }

    @Test(expected = ReservationExpireDateInvalid.class)
    public void create_oneEventDay_ReservationExpireDateInvalid() throws LocationNotAvailableThatDate, DateCantBeInThePast, SectorCapacityOverload, LocationSchemeDoesNotExist, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, SectorDoesNotExist, ParseException {

        eventEventDaysDTO.getEventDays().get(0).setReservationExpireDate("2022-05-05");
        Event createdEvent = eventService.create(eventEventDaysDTO);

        assertEquals(0, eventRepository.findAll().size());
        assertEquals(0, ticketRepository.findAll().size());
        assertEquals(0, eventDaysRepository.findAll().size());
        assertEquals(0, locationRepository.findAll().size());
        assertEquals(0, locationSectorRepository.findAll().size());
    }

    @Test(expected = DateFormatIsNotValid.class)
    public void create_oneEventDay_DateFormatIsNotValid() throws LocationNotAvailableThatDate, DateCantBeInThePast, SectorCapacityOverload, LocationSchemeDoesNotExist, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, SectorDoesNotExist, ParseException {

        eventEventDaysDTO.getEventDays().get(0).setReservationExpireDate("05-05-2022");
        Event createdEvent = eventService.create(eventEventDaysDTO);

        assertEquals(0, eventRepository.findAll().size());
        assertEquals(0, ticketRepository.findAll().size());
        assertEquals(0, eventDaysRepository.findAll().size());
        assertEquals(0, locationRepository.findAll().size());
        assertEquals(0, locationSectorRepository.findAll().size());
    }

    @Test(expected = EventDaysListEmpty.class)
    public void create_oneEventDay_EventDaysListEmpty() throws LocationNotAvailableThatDate, DateCantBeInThePast, SectorCapacityOverload, LocationSchemeDoesNotExist, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, SectorDoesNotExist, ParseException {

        eventEventDaysDTO.setEventDays(new ArrayList<>());
        Event createdEvent = eventService.create(eventEventDaysDTO);

        assertEquals(0, eventRepository.findAll().size());
        assertEquals(0, ticketRepository.findAll().size());
        assertEquals(0, eventDaysRepository.findAll().size());
        assertEquals(0, locationRepository.findAll().size());
        assertEquals(0, locationSectorRepository.findAll().size());
    }

    @Test(expected = SectorDoesNotExist.class)
    public void create_oneEventDay_SectorDoesNotExist() throws LocationNotAvailableThatDate, DateCantBeInThePast, SectorCapacityOverload, LocationSchemeDoesNotExist, ReservationExpireDateInvalid, DateFormatIsNotValid, EventDaysListEmpty, SectorDoesNotExist, ParseException {

        eventEventDaysDTO.getEventDays().get(0).getLocation().getLocationSectors().get(0).setSectorId(10L);
        Event createdEvent = eventService.create(eventEventDaysDTO);

        assertEquals(0, eventRepository.findAll().size());
        assertEquals(0, ticketRepository.findAll().size());
        assertEquals(0, eventDaysRepository.findAll().size());
        assertEquals(0, locationRepository.findAll().size());
        assertEquals(0, locationSectorRepository.findAll().size());
    }

}
