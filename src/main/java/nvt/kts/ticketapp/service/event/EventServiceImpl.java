package nvt.kts.ticketapp.service.event;

import com.google.zxing.WriterException;
import nvt.kts.ticketapp.domain.dto.event.*;
import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.dto.location.SectorForDrawingDTO;
import nvt.kts.ticketapp.domain.model.event.Event;
import nvt.kts.ticketapp.domain.model.event.EventDay;
import nvt.kts.ticketapp.domain.model.event.EventDayState;
import nvt.kts.ticketapp.domain.model.location.*;
import nvt.kts.ticketapp.domain.model.ticket.Ticket;
import nvt.kts.ticketapp.domain.model.user.User;
import nvt.kts.ticketapp.exception.date.DateCantBeInThePast;
import nvt.kts.ticketapp.exception.date.DateFormatIsNotValid;
import nvt.kts.ticketapp.exception.event.*;
import nvt.kts.ticketapp.exception.location.LocationNotAvailableThatDate;
import nvt.kts.ticketapp.exception.location.LocationSectorsDoesNotExistForLocation;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.exception.sector.SectorCapacityOverload;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.exception.sector.SectorWrongType;
import nvt.kts.ticketapp.exception.ticket.NumberOfTicketsException;
import nvt.kts.ticketapp.exception.ticket.ReservationIsNotPossible;
import nvt.kts.ticketapp.exception.ticket.SeatIsNotAvailable;
import nvt.kts.ticketapp.exception.ticket.TicketListCantBeEmpty;
import nvt.kts.ticketapp.repository.event.EventDaysRepository;
import nvt.kts.ticketapp.repository.event.EventRepository;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.ticket.TicketRepository;
import nvt.kts.ticketapp.service.common.email.ticket.TicketEmailService;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import nvt.kts.ticketapp.service.sector.LocationSectorService;
import nvt.kts.ticketapp.service.sector.SectorService;
import nvt.kts.ticketapp.service.ticket.TicketService;
import nvt.kts.ticketapp.util.DateUtil;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static nvt.kts.ticketapp.config.Constants.DATE_FORMAT;
import static nvt.kts.ticketapp.util.DateUtil.*;

@Service
@EnableTransactionManagement
public class EventServiceImpl implements EventService {

    private final TicketEmailService ticketEmailService;
    private final EventRepository eventRepository;
    private final EventDaysRepository eventDaysRepository;
    private final EventDayService eventDayService;
    private final LocationSchemeService locationSchemeService;
    private LocationSectorService locationSectorService;
    private TicketService ticketService;
    private SectorService sectorService;
    private LocationRepository locationRepository;
    private LocationSectorRepository locationSectorRepository;
    private TicketRepository ticketRepository;


    public EventServiceImpl(TicketEmailService ticketEmailService, EventRepository eventRepository, EventDaysRepository eventDaysRepository, EventDayService eventDayService, LocationSchemeService locationSchemeService, LocationSectorService locationSectorService, TicketService ticketService, SectorService sectorService, LocationRepository locationRepository, LocationSectorRepository locationSectorRepository, TicketRepository ticketRepository) {
        this.ticketEmailService = ticketEmailService;
        this.eventRepository = eventRepository;
        this.eventDaysRepository = eventDaysRepository;
        this.eventDayService = eventDayService;
        this.locationSchemeService = locationSchemeService;
        this.locationSectorService = locationSectorService;
        this.ticketService = ticketService;
        this.sectorService = sectorService;
        this.locationRepository = locationRepository;
        this.locationSectorRepository = locationSectorRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Event create(EventEventDaysDTO eventEventDaysDTO) throws DateFormatIsNotValid, LocationSchemeDoesNotExist, SectorDoesNotExist, LocationNotAvailableThatDate, EventDaysListEmpty, SectorCapacityOverload, DateCantBeInThePast, ReservationExpireDateInvalid, ParseException {

        Event event = ObjectMapperUtils.map(eventEventDaysDTO.getEvent(), Event.class);

        List<EventDay> eventDays = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
        List<LocationSector> locationSectors = new ArrayList<>();
        List<Ticket> tickets = new ArrayList<>();


        if (eventEventDaysDTO.getEventDays().isEmpty()) {
            throw new EventDaysListEmpty();
        }

        // create eventDays
        for (EventDayDTO eventDayDTO : eventEventDaysDTO.getEventDays()) {

            Date date = parseDate(eventDayDTO.getDate(), DATE_FORMAT);
            Date reservationExpireDate = parseDate(eventDayDTO.getReservationExpireDate(), DATE_FORMAT);

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
                    for (int i = 0; i < locationSector.getCapacity(); i++) {
                        Ticket ticket = new Ticket(false, locationSector.getSector().getId(), 0, 0, locationSectorsDTO.getPrice(), eventDay, null, locationSectorsDTO.isVip());
                        ticket.setSectorType(SectorType.PARTER);
                        tickets.add(ticket);
                    }
                } else {
                    // generate tickets for locationSector Grandstand
                    for (int row = 1; row <= locationSector.getSector().getRowNum(); row++) {
                        for (int col = 1; col <= locationSector.getSector().getColNum(); col++) {
                            Ticket ticket = new Ticket(false, locationSector.getSector().getId(), row, col, locationSectorsDTO.getPrice(), eventDay, null, locationSectorsDTO.isVip());
                            ticket.setSectorType(SectorType.GRANDSTAND);
                            tickets.add(ticket);
                        }
                    }
                }
            }

        }

        // save event, eventDays, locations, locationSectors
        event = eventRepository.save(event);

        locationRepository.saveAll(locations);
        locationSectorRepository.saveAll(locationSectors);
        eventDaysRepository.saveAll(eventDays);
        ticketRepository.saveAll(tickets);

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

    private void checkAvailabilityOfLocationOnDate(Long locationSchemeId, Date date) throws LocationNotAvailableThatDate, ParseException {

        List<EventDay> eventDays = eventDaysRepository.findAllByLocationId(locationSchemeId);

        if (!eventDays.isEmpty()) {
            for (EventDay eventDay : eventDays) {
                if (DateUtil.datesEqual(eventDay.getDate(), date)) {
                    throw new LocationNotAvailableThatDate();
                }
            }
        }
    }

    public EventDTO update(Long eventId, EventDTO eventDetails) throws EventNotFound {

        Event event = eventRepository.findByIdAndDeletedFalse(eventId).orElseThrow(() -> new EventNotFound(eventId));

        event.setName(eventDetails.getName());
        event.setCategory(eventDetails.getCategory());
        event.setDescription(eventDetails.getDescription());


        Event updatedEvent = eventRepository.save(event);
        return ObjectMapperUtils.map(updatedEvent, EventDTO.class);
    }


    public EventDayUpdateDTO updateEventDay(Long id, EventDayUpdateDTO eventDayDetails) throws EventdayNotFound, DateFormatIsNotValid {

        EventDay eventDay = eventDaysRepository.findByIdAndDeletedFalse(id).
                orElseThrow(() -> new EventdayNotFound(id));

        Date date = parseDate(eventDayDetails.getDate(), DATE_FORMAT);
        Date reservationExpireDate = parseDate(eventDayDetails.getReservationExpirationDate(), DATE_FORMAT);

        eventDay.setDate(date);
        eventDay.setState(eventDayDetails.getEventDayState());
        eventDay.setReservationExpirationDate(reservationExpireDate);


        return ObjectMapperUtils.map(eventDaysRepository.save(eventDay), EventDayUpdateDTO.class);

    }

    @Override
    public EventsDTO findAll(Pageable pageable, String searchQuery, String dateFilter, String typeFilter,
                             String locationFilter) {

        if (searchQuery == null && dateFilter == null && typeFilter == null && locationFilter == null) {
            Page<Event> events = eventRepository.findAll(pageable);

            List<EventDTO> eventDTOList = ObjectMapperUtils.mapAll(events.getContent(), EventDTO.class);

            for (EventDTO eventDTO : eventDTOList) {
                List<EventDay> eventDays = eventDaysRepository.findAllByEventId(eventDTO.getId());

                List<DateAndLocationDTO> dateAndLocationDTOList = new ArrayList<>();
                for (EventDay eventDay : eventDays) {
                    DateAndLocationDTO dateAndLocationDTO = new DateAndLocationDTO();
                    dateAndLocationDTO.setDate(eventDay.getDate().toString());
                    dateAndLocationDTO.setLocation(eventDay.getLocation().getScheme().getName());
                    dateAndLocationDTOList.add(dateAndLocationDTO);
                }
                eventDTO.setDateAndLocationDTO(dateAndLocationDTOList);
            }

            return new EventsDTO(eventDTOList, events.getTotalElements());

        } else {
            Page<Event> events = eventRepository.executeCustomQuery(pageable, searchQuery, dateFilter, typeFilter, locationFilter);

            List<EventDTO> eventDTOList = ObjectMapperUtils.mapAll(events.getContent(), EventDTO.class);

            for (EventDTO eventDTO : eventDTOList) {
                List<EventDay> eventDays = eventDaysRepository.findAllByEventId(eventDTO.getId());

                List<DateAndLocationDTO> dateAndLocationDTOList = new ArrayList<>();
                for (EventDay eventDay : eventDays) {
                    DateAndLocationDTO dateAndLocationDTO = new DateAndLocationDTO();
                    dateAndLocationDTO.setDate(eventDay.getDate().toString());
                    dateAndLocationDTO.setLocation(eventDay.getLocation().getScheme().getName());
                    dateAndLocationDTOList.add(dateAndLocationDTO);
                }
                eventDTO.setDateAndLocationDTO(dateAndLocationDTOList);
            }

            return new EventsDTO(eventDTOList, events.getTotalElements());

        }
    }

    @Override
    public List<EventDayDTOHomePage> getEventDays(Long id) throws EventdayNotFound {
        EventDay eventDay = eventDaysRepository.findByIdAndDeletedFalse(id).
                orElseThrow(() -> new EventdayNotFound(id));

        List<EventDay> eventDays = eventDaysRepository.findAllByEventId(eventDay.getId());

        List<EventDayDTOHomePage> result = new ArrayList<>();
        for (EventDay ed : eventDays) {
            List<LocationSector> locationSectors = locationSectorRepository.findAllByLocationIdAndDeletedFalse(ed.getLocation().getId());

            List<LocationSectorsDTO2> locationSectorsDTO2 = new ArrayList<>();

            for (LocationSector locationSector : locationSectors) {
                LocationSectorsDTO2 ls = new LocationSectorsDTO2(
                        locationSector.getSector().getType(), locationSector.getSector().getId(), locationSector.getPrice(),
                        locationSector.isVip()
                );
                locationSectorsDTO2.add(ls);
            }

            EventDayDTOHomePage eventDayDTOHomePage = new EventDayDTOHomePage(
                    ed.getId(), new EventDTO(ed.getEvent()), ed.getDate().toString(),
                    ed.getReservationExpirationDate().toString(),
                    ed.getState(), ed.getLocation().getScheme().getId(),
                    ed.getLocation().getScheme().getName(),
                    ed.getLocation().getScheme().getAddress(),
                    locationSectorsDTO2
            );
            result.add(eventDayDTOHomePage);
        }

        return result;
    }

    @Override
    public Event findOne(Long id) {
        return eventRepository.getOne(id);
    }

    @Override
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<Ticket> reserve(EventDayReservationDTO eventDayReservationDTO, User user) throws EventDayDoesNotExistOrStateIsNotValid, LocationSectorsDoesNotExistForLocation, SectorNotFound, SectorWrongType, NumberOfTicketsException, SeatIsNotAvailable, ReservationIsNotPossible, IOException, WriterException, TicketListCantBeEmpty {

        EventDay eventDay = eventDayService.getReservableAndBuyableAndDateAfter(eventDayReservationDTO.getEventDayId(), setTimeToMidnight(new Date()));

        // if it is reservation, check reservation date
        if (!eventDayReservationDTO.isPurchase() && eventDay.getReservationExpirationDate().before(setTimeToMidnight(new Date()))) {
            throw new ReservationIsNotPossible();
        }

        Long locationId = eventDay.getLocation().getId();

        List<LocationSector> locationSectors = locationSectorService.get(locationId);

        List<Ticket> reservedParterTickets = reserveParter(eventDayReservationDTO, locationSectors, eventDay, user);
        List<Ticket> reservedGrandstandTickets = reserveGrandstand(eventDayReservationDTO, locationSectors, eventDay, user);

        List<Ticket> tickets = new ArrayList<>();
        tickets.addAll(reservedGrandstandTickets);
        tickets.addAll(reservedParterTickets);


        checkIfEventDayIsSoldOut(eventDay);

        sendMailsForPurchasedTickets(tickets);

        return tickets;
    }


    @Override
    public EventDayBuyingDTO getEventDay(Long id) throws EventdayNotFound {
        EventDay eventDay = eventDaysRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new EventdayNotFound(id));

        List<LocationSector> locationSectors = locationSectorRepository.
                findAllByLocationIdAndDeletedFalse(eventDay.getLocation().getId());


        List<SectorForDrawingDTO> eventDaysSectors = new ArrayList<>();
        for (LocationSector locationSector : locationSectors) {

            List<Ticket> tickets = ticketService.getAvailableTicketsForEventDayAndSector(id, locationSector.getSector().getId());

            int numOfAvailablePlaces = tickets.size();

            SectorForDrawingDTO convertedSector = new SectorForDrawingDTO(locationSector.isDeleted(), locationSector.getSector().getTopLeftX(),
                    locationSector.getSector().getTopLeftY(), locationSector.getSector().getBottomRightX(),
                    locationSector.getSector().getBottomRightY(), locationSector.getCapacity(),
                    locationSector.getSector().getRowNum(), locationSector.getSector().getColNum(),
                    locationSector.getPrice(), locationSector.getSector().getType(), numOfAvailablePlaces);
            convertedSector.setId(locationSector.getSector().getId());

            eventDaysSectors.add(convertedSector);
        }

        return new EventDayBuyingDTO(eventDay.getId(),
                new EventDTO(eventDay.getEvent()), eventDay.getDate().toString(),
                eventDay.getReservationExpirationDate().toString(), eventDay.getState(),
                eventDay.getLocation().getId(), eventDay.getLocation().getScheme().getName(),
                eventDay.getLocation().getScheme().getAddress(), eventDaysSectors);
    }

    @Override
    public double calculateTotalPrice(EventDayReservationDTO eventDayReservationDTO) throws EventDayDoesNotExist {
        double totalPrice = 0;
        EventDay eventDay = eventDayService.findOneById(eventDayReservationDTO.getEventDayId());

        List<LocationSector> locationSectors = locationSectorRepository.findAllByLocationIdAndDeletedFalse(eventDay.getLocation().getId());
        for (LocationSector locationSector: locationSectors) {
            for (ParterDTO parterDTO: eventDayReservationDTO.getParters()){
                if(parterDTO.getSectorId() == locationSector.getSector().getId()){
                    totalPrice += locationSector.getPrice() * parterDTO.getNumberOfTickets();
                }
            }
            for (SeatDTO seatDTO: eventDayReservationDTO.getSeats()) {
                if(seatDTO.getSectorId() == locationSector.getSector().getId()){
                    totalPrice += locationSector.getPrice();
                }
            }
        }

        return totalPrice;
    }

    private List<Ticket> reserveGrandstand(EventDayReservationDTO eventDayReservationDTO, List<LocationSector> locationSectors, EventDay eventDay, User user) throws SectorWrongType, SeatIsNotAvailable, SectorNotFound {

        List<Ticket> reservedTickets = new ArrayList<>();

        for (SeatDTO seatDTO : eventDayReservationDTO.getSeats()) {
            Long sectorId = seatDTO.getSectorId();

            boolean reservationSuccess = false;

            for (LocationSector locationSector : locationSectors) {
                if (locationSector.getSector().getId() != sectorId) {
                    continue;
                }
                // check if that sector is grandstand
                if (locationSector.getSector().getType() != SectorType.GRANDSTAND) {
                    throw new SectorWrongType(sectorId);
                }

                Ticket ticket = ticketService.getAvailableGrandstandTicketForEventDayAndSector(seatDTO, eventDay);
                ticket.setUser(user);
                ticket.setSold(eventDayReservationDTO.isPurchase());
                reservedTickets.add(ticket);
                ticketRepository.save(ticket);
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

        for (ParterDTO parterDTO : eventDayReservationDTO.getParters()) {
            Long sectorId = parterDTO.getSectorId();

            for (LocationSector locationSector : locationSectors) {
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
                    ticket.setSold(eventDayReservationDTO.isPurchase());
                    ticketRepository.save(ticket);
                    reservedTickets.add(ticket);
                }
                return reservedTickets;

            }
            throw new SectorNotFound(sectorId);
        }

        return reservedTickets;
    }


    private void checkIfEventDayIsSoldOut(EventDay eventDay) {

        List<Ticket> tickets = ticketService.getAvailableTickets(eventDay.getId());

        if (!tickets.isEmpty()) {
            return;
        }

        eventDay.setState(EventDayState.SOLD_OUT);
        eventDayService.save(eventDay);

    }

    private void sendMailsForPurchasedTickets(List<Ticket> tickets) throws IOException, WriterException, TicketListCantBeEmpty {

        List<Ticket> purchasedTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (!ticket.isSold()) {
                continue;
            }

            purchasedTickets.add(ticket);
        }

        if (purchasedTickets.isEmpty()) {
            return;
        }

        ticketEmailService.sendEmailForPurchasedTickets(tickets.get(0).getUser().getEmail(), purchasedTickets);

    }


}
