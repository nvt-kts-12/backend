package nvt.kts.ticketapp.service.event;

import nvt.kts.ticketapp.domain.dto.event.*;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.date.DateCantBeInThePast;
import nvt.kts.ticketapp.exception.date.DateFormatIsNotValid;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExist;
import nvt.kts.ticketapp.exception.event.EventDayDoesNotExistOrStateIsNotValid;
import nvt.kts.ticketapp.exception.event.EventDaysListEmpty;
import nvt.kts.ticketapp.exception.event.EventNotFound;
import nvt.kts.ticketapp.exception.event.EventdayNotFound;
import nvt.kts.ticketapp.exception.event.ReservationExpireDateInvalid;
import nvt.kts.ticketapp.exception.location.LocationNotAvailableThatDate;
import nvt.kts.ticketapp.exception.location.LocationSectorsDoesNotExistForLocation;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.exception.sector.SectorCapacityOverload;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.exception.sector.SectorWrongType;
import nvt.kts.ticketapp.exception.ticket.NumberOfTicketsException;
import nvt.kts.ticketapp.exception.ticket.SeatIsNotAvailable;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.user.UserRepository;
import nvt.kts.ticketapp.service.location.LocationService;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import nvt.kts.ticketapp.service.sector.LocationSectorService;
import nvt.kts.ticketapp.service.sector.SectorService;
import nvt.kts.ticketapp.service.ticket.TicketService;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static nvt.kts.ticketapp.config.Constants.DATE_FORMAT;
import static nvt.kts.ticketapp.config.Constants.DATE_TIME_FORMAT;
import static nvt.kts.ticketapp.util.DateUtil.dateInPast;
import static nvt.kts.ticketapp.util.DateUtil.parseDate;

@Service
@EnableTransactionManagement
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventDaysRepository eventDaysRepository;
    private final EventDayService eventDayService;
    private final LocationSchemeService locationSchemeService;
    private final LocationService locationService;
    private final SectorService sectorService;
    private final LocationSectorService locationSectorService;
    private final TicketService ticketService;

    @Autowired
    private UserRepository userRepository;


    public EventServiceImpl(EventRepository eventRepository,EventDaysRepository eventDaysRepository, EventDayService eventDayService, LocationSchemeService locationSchemeService, LocationService locationService, SectorService sectorService, LocationSectorService locationSectorService, TicketService ticketService) {
        this.eventRepository = eventRepository;
        this.eventDaysRepository = eventDaysRepository;
        this.eventDayService = eventDayService;
        this.locationSchemeService = locationSchemeService;
        this.locationService = locationService;
        this.sectorService = sectorService;
        this.locationSectorService = locationSectorService;
        this.ticketService = ticketService;
    }


    @Override
    public Event save(EventEventDaysDTO eventEventDaysDTO) throws DateFormatIsNotValid, LocationSchemeDoesNotExist, SectorDoesNotExist, LocationNotAvailableThatDate, ParseException, EventDaysListEmpty, SectorCapacityOverload, DateCantBeInThePast, ReservationExpireDateInvalid {

        Event event = ObjectMapperUtils.map(eventEventDaysDTO.getEvent(), Event.class);

        List<EventDay> eventDays = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
        List<LocationSector> locationSectors = new ArrayList<>();
        List<Ticket> tickets = new ArrayList<>();


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


                if (locationSector.getSector().getType() == SectorType.PARTER) {
                    // generate tickets for locationSector Parter
                    for(int i = 0; i < locationSector.getCapacity(); i++) {
                        Ticket ticket = new Ticket(false, locationSector.getSector().getId(), 0,0, locationSectorsDTO.getPrice(), eventDay, null, locationSectorsDTO.isVip());
                        tickets.add(ticket);
                    }
                } else {
                    // generate tickets for locationSector Grandstand
                    for (int row = 1; row <= locationSector.getSector().getRowNum(); row++) {
                        for(int col = 1; col <= locationSector.getSector().getColNum(); col++) {
                            Ticket ticket = new Ticket(false, locationSector.getSector().getId(), row, col, locationSectorsDTO.getPrice(), eventDay, null, locationSectorsDTO.isVip());
                            tickets.add(ticket);
                        }
                    }
                }
            }

        }

        // save event, eventDays, locations, locationSectors
        event = eventRepository.save(event);
        locationService.saveAll(locations);
        locationSectorService.saveAll(locationSectors);
        eventDayService.saveAll(eventDays);
        ticketService.saveAll(tickets);

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
    public Page<Event> findAll(Pageable pageable){
            return eventRepository.findAll(pageable);
    }

    @Override
    public Event findOne(Long id){return eventRepository.getOne(id);}

    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<Ticket> reserve(EventDayReservationDTO eventDayReservationDTO, User user) throws EventDayDoesNotExist, EventDayDoesNotExistOrStateIsNotValid, LocationSectorsDoesNotExistForLocation, SectorNotFound, SectorWrongType, NumberOfTicketsException, SeatIsNotAvailable {

        EventDay eventDay = eventDayService.getReservableAndBuyable(eventDayReservationDTO.getEventDayId());

        Long locationId = eventDay.getLocation().getId();

        List<LocationSector> locationSectors = locationSectorService.get(locationId);

        List<Ticket> reservedParterTickets = reserveParter(eventDayReservationDTO, locationSectors, eventDay, user);
        List<Ticket> reservedGrandstandTickets = reserveGrandstand(eventDayReservationDTO, locationSectors, eventDay, user);

        List<Ticket> tickets = new ArrayList<>();
        tickets.addAll(reservedGrandstandTickets);
        tickets.addAll(reservedParterTickets);

        List<Ticket> savedTickets = ticketService.saveAll(tickets);

        EventDay eventDay1 = checkIfEventDayIsSoldOut(eventDay);

        return  tickets;
    }

    private List<Ticket> reserveGrandstand(EventDayReservationDTO eventDayReservationDTO, List<LocationSector> locationSectors, EventDay eventDay, User user) throws SectorWrongType, SeatIsNotAvailable, SectorNotFound {

        List<Ticket> reservedTickets = new ArrayList<>();

        for (SeatDTO seatDTO : eventDayReservationDTO.getSeats()) {
            Long sectorId = seatDTO.getSectorId();

            boolean reservationSuccess = false;

            for(LocationSector locationSector : locationSectors) {
                if (locationSector.getSector().getId() != sectorId) {
                    continue;
                }
                // check if that sector is grandstand
                if (locationSector.getSector().getType() != SectorType.GRANDSTAND) {
                    throw new SectorWrongType(sectorId);
                }

                Ticket ticket = ticketService.getAvailableGrandstandTicketForEventDayAndSector(seatDTO, eventDay);
                ticket.setUser(user);
                reservedTickets.add(ticket);
                reservationSuccess = true;
            }
            if (!reservationSuccess) {
                throw new SectorNotFound(sectorId);
            }
        }

        return reservedTickets;

    }

    private List<Ticket> reserveParter(EventDayReservationDTO eventDayReservationDTO, List<LocationSector> locationSectors, EventDay eventDay, User user) throws SectorWrongType, NumberOfTicketsException, SectorNotFound {

        List<Ticket> reservedTickets = new ArrayList<>();

        for(ParterDTO parterDTO: eventDayReservationDTO.getParters()) {
            Long sectorId = parterDTO.getSectorId();

            for(LocationSector locationSector : locationSectors) {
                if (locationSector.getSector().getId() != sectorId) {
                    continue;
                }

                // check if that sector is parter
                if (locationSector.getSector().getType() != SectorType.PARTER) {
                    throw new SectorWrongType(sectorId);
                }

                List<Ticket> availableTickets = ticketService.getAvailableTicketsForEventDayAndSector(eventDay.getId(), sectorId);

                if (availableTickets.size() < parterDTO.getNumberOfTickets()) {
                    throw new NumberOfTicketsException();
                }

                // parter reservation possible
                for (int i = 0; i < parterDTO.getNumberOfTickets(); i++) {
                    Ticket ticket = availableTickets.get(i);
                    ticket.setUser(user);
                    reservedTickets.add(ticket);
                }
                return reservedTickets;

            }
            throw new SectorNotFound(sectorId);
        }

        return reservedTickets;
    }


    private EventDay checkIfEventDayIsSoldOut(EventDay eventDay) {

        List<Ticket> tickets = ticketService.getAvailableTickets(eventDay.getId());

        if (!tickets.isEmpty()) {
            return eventDay;
        }

        eventDay.setState(EventDayState.SOLD_OUT);
        return  eventDayService.save(eventDay);

    }

}
