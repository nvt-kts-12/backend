package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.domain.dto.event.EventDTO;
import nvt.kts.ticketapp.domain.dto.event.EventDayDTO;
import nvt.kts.ticketapp.domain.dto.event.EventEventDaysDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.exception.location.LocationNotAvailableThatDate;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.service.ticket.TicketServiceImpl;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.html.Option;
import java.text.ParseException;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventServiceJUnitTest {

    @MockBean
    EventRepository eventRepository;
    @MockBean
    TicketRepository ticketRepository;
    @MockBean
    EventDaysRepository eventDaysRepository;

    @Autowired
    EventServiceImpl eventService;
    @Autowired
    TicketServiceImpl ticketService;
    @Autowired
    EventDayServiceImpl eventDayService;


    ObjectMapperUtils mapper;

    @Before
    public void setup() {

        //events
        List<Event> events = new ArrayList<>();
        events.add(new Event("name", EventCategory.SPORT, "good"));
        events.add(new Event("name2", EventCategory.CULTURAL, "good2"));
        Mockito.when(eventRepository.findAll()).thenReturn(events);

        Event e3 = new Event("name3", EventCategory.ENTERTAINMENT, "good3");
        Event e4 = new Event("name4", EventCategory.SPORT, "good4");
        e3.setId(15L);
        e4.setId(6L);
        Mockito.when(eventRepository.save(e4)).thenReturn(e4);
        Mockito.when(eventRepository.getOne(e4.getId())).thenReturn(e4);
        Mockito.when(eventRepository.findByIdAndDeletedFalse(e4.getId())).thenReturn(Optional.of(e4));
        Mockito.when(eventRepository.findById(2L)).thenReturn(Optional.of(e3));

        //null events
        Mockito.when(eventRepository.getOne(501L)).thenReturn(null);
        Mockito.when(eventRepository.getOne(500L)).thenReturn(null);


        //tickets, eventdays
        LocationScheme scheme = new LocationScheme("petrovaradin", "Novi Sad");
        Location location = new Location(scheme);
        EventDay eventDay = new EventDay(new Date(2020 - 01 - 15), location, new Date(2020 - 02 - 15), EventDayState.SOLD_OUT, e3);
        eventDay.setId(1L);

        Ticket t1 = new Ticket(false, 1L, 1, 1, 123, new EventDay(), new User(), true);
        t1.setDeleted(false);

        Mockito.when(eventDaysRepository.findOneByIdAndStateAndDateBefore(eventDay.getId(), EventDayState.RESERVABLE_AND_BUYABLE, eventDay.getDate())).thenReturn(Optional.of(eventDay));
        Mockito.when(eventDaysRepository.findById(eventDay.getId())).thenReturn(Optional.of(eventDay));


        //Ticket t2 = new Ticket(true,2L,2,2,1234,new EventDay(),new User(),false);

    }

    @Test
    public void findAllTest() {

        List<Event> events = eventService.findAllEvents();
        assertNotNull(events);
        assertThat(events).hasSize(2);
        assertThat(events.get(0).getName()).isEqualTo("name");
        assertThat(events.get(0).getCategory()).isEqualTo(EventCategory.SPORT);
        assertThat(events.get(0).getDescription()).isEqualTo("good");

    }

    @Test
    public void getOneTest() {

        Event event = eventService.findOne(6L);
        assertNotNull(event);
        assertThat(event.getId()).isEqualTo(6L);
        assertThat(event.getName()).isEqualTo("name4");
        assertThat(event.getDescription()).isEqualTo("good4");
        assertThat(event.getCategory()).isEqualTo(EventCategory.SPORT);

    }

    @Test
    public void getOneTest_expectedNull() {

        Event event = eventService.findOne(501L);
        assertNull(event);
    }
/*
    @Test
    public void save() {

        EventDTO eventDTO = new EventDTO(1L, "good", "name", EventCategory.CULTURAL);
        List<EventDayDTO> eventDayDTOS = new ArrayList<>();

        EventEventDaysDTO eventEventDaysDTO = new EventEventDaysDTO(eventDTO, eventDayDTOS);
        eventEventDaysDTO.setEvent(eventDTO);

        EventDTO event1 = eventEventDaysDTO.getEvent();
        Event event = ObjectMapperUtils.map(event1, Event.class);


        when(eventRepository.save(event)).thenReturn(event);
        assertThat(event).isNotNull();
        assertThat(event.getId().equals(1L));
        assertThat(event.getName().equals("name"));
        assertThat(event.getDescription().equals("good"));
        assertThat(event.getCategory().equals(EventCategory.CULTURAL));

    }
*/
    @Test
    public void update() throws EventNotFound {

        EventDTO eventForChange = new EventDTO(6L, "good", "name", EventCategory.SPORT);
        EventDTO changeData = new EventDTO(2L, "good2", "name222", EventCategory.SPORT);

        EventDTO updatedEvent = eventService.update(eventForChange.getId(), changeData);
        assertNotNull(updatedEvent);
        assertThat(updatedEvent.getDescription()).isEqualTo(changeData.getDescription());
        assertThat(updatedEvent.getName()).isEqualTo(changeData.getName());
        assertThat((updatedEvent.getCategory())).isEqualTo(changeData.getCategory());

    }

}