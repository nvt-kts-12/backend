package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.event.LocationDTO;
import nvt.kts.ticketapp.domain.dto.location.PlainLocationDTO;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationSchemeRepository locationSchemeRepository;

    public LocationServiceImpl(LocationRepository locationRepository, LocationSchemeRepository locationSchemeRepository) {
        this.locationRepository = locationRepository;
        this.locationSchemeRepository = locationSchemeRepository;
    }

    @Override
    public void saveAll(List<Location> locations) {
        locationRepository.saveAll(locations);
    }

}
