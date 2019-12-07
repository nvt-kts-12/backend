package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventCategory;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExist;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExistOrStateIsNotValid;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventDayServiceJUnitTest {

    @MockBean
    EventDaysRepository eventDaysRepository;

    @Autowired
    EventDayService eventDayService;

    EventDay global;

    @Before
    public void setup(){
        Event e1 = new Event("name1", EventCategory.ENTERTAINMENT, "good2");
        Event e2 = new Event("name2", EventCategory.SPORT, "good2");
        LocationScheme lc = new LocationScheme("scheme","novisad");
        Location location = new Location(lc);

        EventDay eventDay1 = new EventDay(new Date(2-1-2019),location,new Date(3-1-2019), EventDayState.RESERVABLE_AND_BUYABLE,e1);
        eventDay1.setId(1L);
        global= new EventDay(new Date(3-1-2019),location,new Date(4-1-2019), EventDayState.SOLD_OUT,e2);

        List<EventDay> eventDays = new ArrayList<>();
        eventDays.add(eventDay1);
        eventDays.add(global);

        Mockito.when(eventDaysRepository.save(eventDay1)).thenReturn(eventDay1);
        Mockito.when(eventDaysRepository.findAll()).thenReturn(eventDays);
        Mockito.when(eventDaysRepository.saveAll(eventDays)).thenReturn(eventDays);
        Mockito.when(eventDaysRepository.findById(eventDay1.getId())).thenReturn(java.util.Optional.of(eventDay1));
        Mockito.when(eventDaysRepository.findOneByIdAndStateAndDateBefore(eventDay1.getId(),EventDayState.RESERVABLE_AND_BUYABLE,eventDay1.getDate())).thenReturn(java.util.Optional.of(eventDay1));
    }

    @Test
    public void save(){
        eventDayService.save(global);
        assertNotNull(global);
    }

    @Test
    public void findAll()
    {
        List<EventDay> eventDays = eventDayService.findAll();
        assertNotNull(eventDays);
    }

    @Test
    public void saveAll(){
        List<EventDay> eventDays = eventDayService.findAll();
        eventDayService.saveAll(eventDays);
        assertNotNull(eventDays);
        assertThat(eventDays.get(0).getState().equals(EventDayState.RESERVABLE_AND_BUYABLE));
        assertThat(eventDays.get(1).getState().equals(EventDayState.SOLD_OUT));
    }

    @Test
    public void findById() throws EventDayDoesNotExist {
        EventDay eventDay = eventDayService.findOneById(1L);
        assertNotNull(eventDay);
    }

    @Test
    public void getReservableAndBuyableAndDateBefore() throws EventDayDoesNotExist, EventDayDoesNotExistOrStateIsNotValid {
        EventDay eventDay = eventDayService.getReservableAndBuyableAndDateBefore(1L,new Date(2-1-2019));
        assertNotNull(eventDay);
    }





}
