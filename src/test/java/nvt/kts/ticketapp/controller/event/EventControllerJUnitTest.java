package nvt.kts.ticketapp.controller.event;
import nvt.kts.ticketapp.domain.dto.event.*;
import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.dto.location.LocationSchemeSectorsDTO;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.dto.user.UserDTO;
import nvt.kts.ticketapp.domain.dto.user.UserRegistrationDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.SectorType;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.date.DateCantBeInThePast;
import nvt.kts.ticketapp.exception.date.DateFormatIsNotValid;
import nvt.kts.ticketapp.exception.event.EventDaysListEmpty;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.exception.event.ReservationExpireDateInvalid;
import nvt.kts.ticketapp.exception.location.LocationNotAvailableThatDate;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.exception.sector.SectorCapacityOverload;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.exception.user.*;
import nvt.kts.ticketapp.service.event.EventServiceImpl;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import nvt.kts.ticketapp.service.location.LocationSchemeServiceImpl;
import nvt.kts.ticketapp.service.user.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

import javax.swing.text.html.Option;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)

public class EventControllerJUnitTest {

    @LocalServerPort
    int randomServerPort;

    private static final String URL_PREFIX = "/api/event";

    @MockBean
    private EventServiceImpl eventServiceMocked;

    @MockBean
    private LocationSchemeServiceImpl locationSchemeService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    UserServiceImpl userServiceMocked;

    private static UserRegistrationDTO userRegistrationDTO;
    private static EventEventDaysDTO eventEventDaysDTO;

    @Before
    public void setUp() throws EventNotFound, SectorCapacityOverload, LocationSchemeDoesNotExist, ReservationExpireDateInvalid, LocationNotAvailableThatDate, DateFormatIsNotValid, ParseException, EventDaysListEmpty, DateCantBeInThePast, SectorDoesNotExist {

        userRegistrationDTO = new UserRegistrationDTO(null,"username","password","firstName","lastName","email@gmail.com");
        //event details to update original event
        EventDTO eventDetails = new EventDTO();
        eventDetails.setName("NAMEEEE");
        eventDetails.setDescription("GOOOD");
        eventDetails.setCategory(EventCategory.CULTURAL);
        eventDetails.setId(501L);

        //SECTOR
        LocationSectorsDTO locationSectorsDTO = new LocationSectorsDTO(1L,20,200,false);
        List<LocationSectorsDTO> locationSectorsDTOS = new ArrayList<LocationSectorsDTO>();
        locationSectorsDTOS.add(locationSectorsDTO);
        LocationDTO locationDTO = new LocationDTO(1L,locationSectorsDTOS);

        LocationSchemeDTO lcDTO = new LocationSchemeDTO(1L,false,"name","address");
        List<LocationSchemeDTO> locationSchemeDTOS = new ArrayList<LocationSchemeDTO>();
        locationSchemeDTOS.add(lcDTO);
        Mockito.when(locationSchemeService.getAll()).thenReturn(locationSchemeDTOS);

        //EVENT DAY
        EventDayDTO eventDayDTO = new EventDayDTO(1L,"1-1-2020",locationDTO,"1-2-2020",EventDayState.RESERVABLE_AND_BUYABLE);
        List<EventDayDTO> eventDayDTOS = new ArrayList<EventDayDTO>();

        //FOR SAVE EVENT
        eventEventDaysDTO = new EventEventDaysDTO(eventDetails,eventDayDTOS);

    }


    @Test
    public void save() throws SectorCapacityOverload, LocationSchemeDoesNotExist, ReservationExpireDateInvalid, LocationNotAvailableThatDate, DateFormatIsNotValid, ParseException, EventDaysListEmpty, DateCantBeInThePast, SectorDoesNotExist {

        Event e = new Event("name",EventCategory.SPORT,"good");
        Mockito.when(eventServiceMocked.save(Mockito.any(EventEventDaysDTO.class))).thenReturn(e);
        ResponseEntity<EventDTO> response = testRestTemplate.postForEntity("/api/event/save",eventEventDaysDTO, EventDTO.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        EventDTO eventDTO  = response.getBody();
        assertNotNull(eventDTO);
        assertEquals(eventDTO.getName(),e.getName());
    }


    @Test
    public void GetAllSchemes() {

        String url = "/api/locationScheme/";
        ResponseEntity<List<LocationSchemeDTO>> response = testRestTemplate.exchange(url,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<LocationSchemeDTO>>() {
                });
        List<LocationSchemeDTO> schemes = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(schemes.get(0).getName(), "name");

    }


    @Test
    public void EditEventValidTest() throws EventNotFound {

        EventDTO eventDetails = new EventDTO();
        eventDetails.setName("NAMEEEE");
        eventDetails.setDescription("GOOOD");
        eventDetails.setCategory(EventCategory.CULTURAL);

        Mockito.when(eventServiceMocked.update(anyLong(),any(EventDTO.class))).thenReturn(eventDetails);

        String url = "http://localhost:" + randomServerPort + URL_PREFIX + "/1";
        HttpEntity<EventDTO> editEventResponse = new HttpEntity<EventDTO>(eventDetails);
        ResponseEntity<EventDTO> response = testRestTemplate.exchange(url, HttpMethod.PUT, editEventResponse, EventDTO.class);
        EventDTO event = editEventResponse.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

}

