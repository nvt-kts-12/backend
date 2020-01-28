package nvt.kts.ticketapp.service.event.price;

import nvt.kts.ticketapp.domain.dto.event.EventDayReservationDTO;
import nvt.kts.ticketapp.domain.dto.event.ParterDTO;
import nvt.kts.ticketapp.domain.dto.event.SeatDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExist;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.service.event.EventDayService;
import nvt.kts.ticketapp.service.event.EventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CalculatePriceUnitTest {


    @Autowired
    private EventService eventService;

    @MockBean
    private EventDayService eventDayService;

    @MockBean
    private LocationSectorRepository locationSectorRepository;

    private static EventDay eventDay;
    private static Location location;
    private static LocationScheme locationScheme;
    private static Sector sector1;
    private static Sector sector2;
    private static LocationSector locationSector1;
    private static LocationSector locationSector2;

    private static EventDayReservationDTO eventDayReservationDTO;
    private static EventDayReservationDTO eventDayReservationDTO2;

    private static final Long EXISTING_EVENTDAY_ID = 1L;
    private static final Long EXISTING_LOCATION_ID = 1L;
    private static final Long EXISTING_LOCATION_SECTOR_ID_01 = 1L;
    private static final Long EXISTING_LOCATION_SECTOR_ID_02 = 2L;

    public static final Long NONEXISTENT_EVENTDAY_ID = 2L;


    @Before
    public void setUp() throws EventDayDoesNotExist {

        locationScheme = new LocationScheme("Scheme name", "Scheme address");
        location = new Location(locationScheme);
        location.setId(EXISTING_LOCATION_ID);
        Event event = new Event("Event name", EventCategory.SPORT, "description");
        eventDay = new EventDay(new Date(), location, new Date(), EventDayState.RESERVABLE_AND_BUYABLE, event);

        sector1 = new Sector(0, 0, 0, 0,
                100, 10, 10, SectorType.GRANDSTAND, locationScheme);
        sector1.setId(EXISTING_LOCATION_SECTOR_ID_01);
        sector2 = new Sector(0, 0, 0, 0,
                100, 0, 0, SectorType.PARTER, locationScheme);
        sector2.setId(EXISTING_LOCATION_SECTOR_ID_02);

        locationSector1 = new LocationSector(sector1, location, 120, 100, false);
        locationSector2 = new LocationSector(sector2, location, 320, 100, false);

        Mockito.when(eventDayService.findOneById(EXISTING_EVENTDAY_ID)).thenReturn(eventDay);
        Mockito.when(locationSectorRepository.findAllByLocationIdAndDeletedFalse(EXISTING_LOCATION_ID))
                .thenReturn(Arrays.asList(locationSector1, locationSector2));
        Mockito.when(eventDayService.findOneById(NONEXISTENT_EVENTDAY_ID)).thenThrow(new EventDayDoesNotExist());

        List<SeatDTO> seatDTOS = new ArrayList<SeatDTO>() {
            {
                add(new SeatDTO(EXISTING_LOCATION_SECTOR_ID_01, 1, 1));
                add(new SeatDTO(EXISTING_LOCATION_SECTOR_ID_01, 2, 1));
                add(new SeatDTO(EXISTING_LOCATION_SECTOR_ID_01, 3, 3));
                add(new SeatDTO(EXISTING_LOCATION_SECTOR_ID_01, 4, 4));
            }
        };
        List<ParterDTO> parterDTOS = new ArrayList<ParterDTO>() {
            {
                add(new ParterDTO(EXISTING_LOCATION_SECTOR_ID_02, 6));
            }
        };
        eventDayReservationDTO = new EventDayReservationDTO(EXISTING_EVENTDAY_ID, parterDTOS,
                seatDTOS, false);
        eventDayReservationDTO2 = new EventDayReservationDTO(NONEXISTENT_EVENTDAY_ID, parterDTOS,
                seatDTOS, false);
    }

    @Test
    public void calculateTotalPrice_Positive() throws EventDayDoesNotExist {
        double totalPrice = eventService.calculateTotalPrice(eventDayReservationDTO);

        assertNotNull(totalPrice);
        assertEquals(locationSector1.getPrice() * 4 + locationSector2.getPrice() * 6, totalPrice, 2);
        verify(eventDayService, Mockito.times(1)).findOneById(EXISTING_EVENTDAY_ID);
        verify(locationSectorRepository, Mockito.times(1)).findAllByLocationIdAndDeletedFalse(EXISTING_LOCATION_ID);
    }

    @Test(expected = EventDayDoesNotExist.class)
    public void calculateTotalPrice_Negative() throws EventDayDoesNotExist {
        eventService.calculateTotalPrice(eventDayReservationDTO2);
    }


}
