package nvt.kts.ticketapp.service.sector;

import nvt.kts.ticketapp.domain.dto.event.LocationSectorsDTO;
import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.exception.location.LocationSectorsDoesNotExistForLocation;
import nvt.kts.ticketapp.exception.sector.LocationSectorDoesNotExist;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationSectorServiceImpl implements LocationSectorService {

    private final LocationSectorRepository locationSectorRepository;

    public LocationSectorServiceImpl(LocationSectorRepository locationSectorRepository) {
        this.locationSectorRepository = locationSectorRepository;
    }

    @Override
    public List<LocationSectorsDTO> saveAll(List<LocationSector> locationSectors) {
        locationSectorRepository.saveAll(locationSectors);
        return ObjectMapperUtils.mapAll(locationSectors, LocationSectorsDTO.class);
    }

    @Override
    public List<LocationSector> get(Long locationId) throws LocationSectorsDoesNotExistForLocation {
        List<LocationSector> locationSectors = locationSectorRepository.findAllByLocationIdAndDeletedFalse(locationId);
        if (locationSectors.isEmpty()) {
            throw new LocationSectorsDoesNotExistForLocation(locationId);
        }

        return locationSectors;
    }

    @Override
    public LocationSectorsDTO getOne(Long sectorId) throws LocationSectorDoesNotExist {
        LocationSector foundSector = locationSectorRepository.findById(sectorId).
                orElseThrow(() -> new LocationSectorDoesNotExist(sectorId));

        return ObjectMapperUtils.map(foundSector, LocationSectorsDTO.class);
    }


}
