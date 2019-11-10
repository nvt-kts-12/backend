package nvt.kts.ticketapp.service.sector;

import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.exception.location.LocationSectorsDoesNotExistForLocation;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationSectorServiceImpl implements LocationSectorService {

    private final LocationSectorRepository locationSectorRepository;

    public LocationSectorServiceImpl(LocationSectorRepository locationSectorRepository) {
        this.locationSectorRepository = locationSectorRepository;
    }

    @Override
    public void saveAll(List<LocationSector> locationSectors) {
        locationSectorRepository.saveAll(locationSectors);
    }

    @Override
    public List<LocationSector> get(Long locationId) throws LocationSectorsDoesNotExistForLocation {
        List<LocationSector> locationSectors = locationSectorRepository.findAllByLocationIdAndDeletedFalse(locationId);
        if (locationSectors.isEmpty()) {
            throw new LocationSectorsDoesNotExistForLocation(locationId);
        }

        return locationSectors;
    }


}
