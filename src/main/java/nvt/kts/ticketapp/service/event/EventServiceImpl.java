package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.domain.dto.event.EventDayDTO;
import nvt.kts.ticketapp.domain.dto.event.EventEventDaysDTO;
import nvt.kts.ticketapp.domain.dto.event.LocationSectorsDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.exception.date.DateCantBeInPast;
import nvt.kts.ticketapp.exception.date.DateFormatNotValid;
import nvt.kts.ticketapp.exception.event.EventDaysListEmpty;
import nvt.kts.ticketapp.exception.location.LocationNotAvailableThatDate;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeNotExist;
import nvt.kts.ticketapp.exception.sector.SectorCapacityOverload;
import nvt.kts.ticketapp.exception.sector.SectorNotExist;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.service.location.LocationService;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import nvt.kts.ticketapp.service.sector.LocationSectorService;
import nvt.kts.ticketapp.service.sector.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static nvt.kts.ticketapp.util.DateUtil.*;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventDayService eventDayService;
    private final LocationSchemeService locationSchemeService;
    private final LocationService locationService;
    private final SectorService sectorService;
    private final LocationSectorService locationSectorService;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, EventDayService eventDayService, LocationSchemeService locationSchemeService, LocationService locationService, SectorService sectorService, LocationSectorService locationSectorService) {
        this.eventRepository = eventRepository;
        this.eventDayService = eventDayService;
        this.locationSchemeService = locationSchemeService;
        this.locationService = locationService;
        this.sectorService = sectorService;
        this.locationSectorService = locationSectorService;
    }

    @Override
    public Event save(EventEventDaysDTO eventEventDaysDTO) throws DateFormatNotValid, LocationSchemeNotExist, SectorNotExist, LocationNotAvailableThatDate, ParseException, EventDaysListEmpty, SectorCapacityOverload, DateCantBeInPast, ReservationExpireDateInvalid {

        Event event = new Event(eventEventDaysDTO.getEvent().getName(), eventEventDaysDTO.getEvent().getCategory(), eventEventDaysDTO.getEvent().getDescription());

        List<EventDay> eventDays = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
        List<LocationSector> locationSectors = new ArrayList<>();

        if (eventEventDaysDTO.getEventDays().isEmpty()) {
            throw new EventDaysListEmpty();
        }

        // create eventDays
        for (EventDayDTO eventDayDTO: eventEventDaysDTO.getEventDays()) {

            Date date = parseDate(eventDayDTO.getDate());
            Date reservationExpireDate = parseDate(eventDayDTO.getReservationExpireDate());

            // check if dates in past or reservationExpireDate is before event date
            checkDates(date, reservationExpireDate);

            Long locationSchemeId = eventDayDTO.getLocation().getLocationSchemeId();
            LocationScheme locationScheme = locationSchemeService.findOneById(locationSchemeId);

            // check if a location is available that date
            checkAvailabilityOfLocationOnDate(locationScheme.getId(), date);

            Location location = new Location(locationScheme);
            locations.add(location);

            EventDay eventDay = new EventDay(date, location, reservationExpireDate, EventDayState.NOT_IN_SALE, event);
            eventDays.add(eventDay);

            // create locationSectors
            for (LocationSectorsDTO locationSectorsDTO : eventDayDTO.getLocation().getLocationSectors()) {

                Sector sector = sectorService.findOneById(locationSectorsDTO.getSectorId());

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

    private void checkDates(Date date, Date reservationExpireDate) throws DateCantBeInPast, ReservationExpireDateInvalid {

        if (dateInPast(date) || dateInPast(reservationExpireDate)) {
            throw new DateCantBeInPast();
        }

        if (reservationExpireDate.after(date)) {
            throw new ReservationExpireDateInvalid();
        }

    }

    private void checkAvailabilityOfLocationOnDate(Long locationSchemeId, Date date) throws ParseException, LocationNotAvailableThatDate {

        List<EventDay> eventDays = eventDayService.getAll();

        for (EventDay eventDay: eventDays) {
            if (datesEqual(eventDay.getDate(), date)) {
                if (eventDay.getLocation().getScheme().getId() == locationSchemeId) {
                    throw new LocationNotAvailableThatDate();
                }
            }
        }
    }

    @Override
    public Page<Event> findAll(Pageable pageable){
            return eventRepository.findAll(pageable);
    }

    @Override
    public Event findOne(Long id){return eventRepository.getOne(id);}


}
