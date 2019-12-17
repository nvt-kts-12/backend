package nvt.kts.ticketapp.service.sector;

import nvt.kts.ticketapp.domain.dto.event.LocationSectorsDTO;
import nvt.kts.ticketapp.domain.dto.location.LocationSectorDTO;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.exception.location.LocationNotFound;
import nvt.kts.ticketapp.exception.location.LocationSectorsDoesNotExistForLocation;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import nvt.kts.ticketapp.exception.sector.LocationSectorDoesNotExist;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationSectorServiceImpl implements LocationSectorService {

    private final LocationSectorRepository locationSectorRepository;
    private final SectorRepository sectorRepository;
    private final LocationRepository locationRepository;

    public LocationSectorServiceImpl(LocationSectorRepository locationSectorRepository, SectorRepository sectorRepository,
                        LocationRepository locationRepository) {
        this.locationSectorRepository = locationSectorRepository;
        this.sectorRepository = sectorRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<LocationSectorDTO> saveAll(List<LocationSectorDTO> locationSectors) throws LocationNotFound, SectorNotFound {
        List<LocationSectorDTO> savedSectors = new ArrayList<>();

        for (LocationSectorDTO locationSector: locationSectors) {
            Long sectorId = locationSector.getSectorId();
            Long locationId = locationSector.getLocationId();

            Sector sector = sectorRepository.findByIdAndDeletedFalse(sectorId)
                    .orElseThrow(() -> new SectorNotFound(sectorId));
            Location location = locationRepository.findByIdAndDeletedFalse(locationId)
                    .orElseThrow(() -> new LocationNotFound(locationId));


            LocationSector savedSector = locationSectorRepository
                    .save(new LocationSector(sector, location, locationSector.getPrice(),
                            locationSector.getCapacity(), locationSector.isVip()));

            savedSectors.add(ObjectMapperUtils.map(savedSector, LocationSectorDTO.class));
        }
        return savedSectors;
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
    public LocationSectorDTO getOne(Long sectorId) throws LocationSectorDoesNotExist {
        LocationSector foundSector = locationSectorRepository.findById(sectorId).
                orElseThrow(() -> new LocationSectorDoesNotExist(sectorId));

        return ObjectMapperUtils.map(foundSector, LocationSectorDTO.class);
    }


}
