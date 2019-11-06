package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.domain.dto.event.*;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.exception.date.DateCantBeInThePast;
import nvt.kts.ticketapp.exception.date.DateFormatIsNotValid;
import nvt.kts.ticketapp.exception.event.EventDaysListEmpty;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.exception.event.EventdayNotFound;
import nvt.kts.ticketapp.exception.event.ReservationExpireDateInvalid;
import nvt.kts.ticketapp.exception.location.LocationNotAvailableThatDate;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.exception.sector.SectorCapacityOverload;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.service.location.LocationService;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import nvt.kts.ticketapp.service.sector.LocationSectorService;
import nvt.kts.ticketapp.service.sector.SectorService;
import nvt.kts.ticketapp.util.DateUtil;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.swing.text.html.parser.Entity;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static nvt.kts.ticketapp.config.Constants.DATE_FORMAT;
import static nvt.kts.ticketapp.config.Constants.DATE_TIME_FORMAT;
import static nvt.kts.ticketapp.util.DateUtil.*;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventDaysRepository eventDaysRepository;
    private final EventDayService eventDayService;
    private final LocationSchemeService locationSchemeService;
    private final LocationService locationService;
    private final SectorService sectorService;
    private final LocationSectorService locationSectorService;
    private DateUtil dateUtil;
    private EntityManager em;

    public EventServiceImpl(EventRepository eventRepository,EventDaysRepository eventDaysRepository, EventDayService eventDayService, LocationSchemeService locationSchemeService, LocationService locationService, SectorService sectorService, LocationSectorService locationSectorService, EntityManager em) {
        this.eventRepository = eventRepository;
        this.eventDaysRepository = eventDaysRepository;
        this.eventDayService = eventDayService;
        this.locationSchemeService = locationSchemeService;
        this.locationService = locationService;
        this.sectorService = sectorService;
        this.locationSectorService = locationSectorService;
        this.em = em;
    }


    @Override
    public Event save(EventEventDaysDTO eventEventDaysDTO) throws DateFormatIsNotValid, LocationSchemeDoesNotExist, SectorDoesNotExist, LocationNotAvailableThatDate, ParseException, EventDaysListEmpty, SectorCapacityOverload, DateCantBeInThePast, ReservationExpireDateInvalid {

        Event event = ObjectMapperUtils.map(eventEventDaysDTO.getEvent(), Event.class);

        List<EventDay> eventDays = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
        List<LocationSector> locationSectors = new ArrayList<>();

        if (eventEventDaysDTO.getEventDays().isEmpty()) {
            throw new EventDaysListEmpty();
        }

        // create eventDays
        for (EventDayDTO eventDayDTO: eventEventDaysDTO.getEventDays()) {

            Date date = parseDate(eventDayDTO.getDate(),DATE_TIME_FORMAT);
            Date reservationExpireDate = parseDate(eventDayDTO.getReservationExpireDate(),DATE_TIME_FORMAT);

            // check if dates in past or reservationExpireDate is before event date
            checkDates(date, reservationExpireDate);

            Long locationSchemeId = eventDayDTO.getLocation().getLocationSchemeId();
            LocationScheme locationScheme = locationSchemeService.getScheme(locationSchemeId);

            // check if a location is available that date
            checkAvailabilityOfLocationOnDate(locationScheme.getId(), date);

            Location location = new Location(locationScheme);
            locations.add(location);

            EventDay eventDay = new EventDay(date, location, reservationExpireDate, EventDayState.NOT_IN_SALE, event);
            eventDays.add(eventDay);

            // create locationSectors
            for (LocationSectorsDTO locationSectorsDTO : eventDayDTO.getLocation().getLocationSectors()) {

                Sector sector = sectorService.getSector(locationSectorsDTO.getSectorId());

                if (sector.getCapacity() < locationSectorsDTO.getCapacity()) {
                    throw new SectorCapacityOverload();
                }
                LocationSector locationSector = new LocationSector(sector, location, locationSectorsDTO.getPrice(), locationSectorsDTO.getCapacity(), locationSectorsDTO.isVip());
                locationSectors.add(locationSector);
            }

        }

        // save event, eventDays, locations, locationSectors
        event = eventRepository.save(event);
        locationService.saveAll(locations);
        locationSectorService.saveAll(locationSectors);
        eventDayService.saveAll(eventDays);

        return event;
    }

    private void checkDates(Date date, Date reservationExpireDate) throws DateCantBeInThePast, ReservationExpireDateInvalid {

        if (dateInPast(date) || dateInPast(reservationExpireDate)) {
            throw new DateCantBeInThePast();
        }

        if (reservationExpireDate.after(date)) {
            throw new ReservationExpireDateInvalid();
        }

    }

    private void checkAvailabilityOfLocationOnDate(Long locationSchemeId, Date date) throws ParseException, LocationNotAvailableThatDate {

        List<EventDay> eventDays = eventDayService.findAllByDate(date);

        if (!eventDays.isEmpty()) {
            for (EventDay eventDay: eventDays) {
                if (eventDay.getLocation().getScheme().getId() == locationSchemeId) {
                    throw new LocationNotAvailableThatDate();
                }
            }
        }
    }

    public EventDTO update(Long eventId,EventDTO eventDetails) throws EventNotFound{

        Event event = eventRepository.findByIdAndDeletedFalse(eventId).orElseThrow(() -> new EventNotFound(eventId));

        event.setName(eventDetails.getName());
        event.setCategory(eventDetails.getCategory());
        event.setDescription(eventDetails.getDescription());


        Event updatedEvent = eventRepository.save(event);
        return ObjectMapperUtils.map(updatedEvent, EventDTO.class);
    }


    public EventDayUpdateDTO updateEventDay(EventDayUpdateDTO eventDayDetails) throws EventdayNotFound,DateFormatIsNotValid{

        EventDay eventDay = eventDaysRepository.findByIdAndDeletedFalse(eventDayDetails.getId()).
                orElseThrow(()-> new EventdayNotFound(eventDayDetails.getId()));

        Date date = parseDate(eventDayDetails.getDate(),DATE_FORMAT);
        Date reservationExpireDate = parseDate(eventDayDetails.getReservationExpirationDate(),DATE_FORMAT);

        eventDay.setDate(date);
        eventDay.setState(eventDayDetails.getEventDayState());
        eventDay.setReservationExpirationDate(reservationExpireDate);


       return ObjectMapperUtils.map(eventDaysRepository.save(eventDay), EventDayUpdateDTO.class);

    }




    @Override
    public Page<Event> findAll(Pageable pageable, String searchQuery, String dateFilter, String typeFilter) {

        String queryString = "select e from Event e where ";

        boolean andNeeded = false;

        if (searchQuery != null) {
            String queryAddition = "e.name like '" + searchQuery + "'";
            queryString += queryAddition;
            andNeeded = true;
        }

        if (dateFilter != null) {
            if (andNeeded) {
                queryString += " and ";
            }
            String queryAddition = "e.id in (select ed.event from EventDay ed where ed.date like '" + dateFilter + "')";
            queryString += queryAddition;
            andNeeded = true;
        }

        if (typeFilter != null) {
            if (andNeeded) {
                queryString += " and ";
            }
            String queryAddition = "e.category like '" + typeFilter + "'";
            queryString += queryAddition;
        }

        if (searchQuery == null && dateFilter == null && typeFilter == null) {
            return eventRepository.findAll(pageable);
        } else {
            TypedQuery<Event> query = em.createQuery(queryString, Event.class)
                    .setMaxResults(pageable.getPageSize())
                    .setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            return new PageImpl<>(query.getResultList());
        }
    }

    @Override
    public Event findOne(Long id){return eventRepository.getOne(id);}


}
