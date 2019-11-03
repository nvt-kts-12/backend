package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void saveAll(List<Location> locations) {
        locationRepository.saveAll(locations);
    }
}
