package nvt.kts.ticketapp.service.sector;

import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.exception.location.LocationSectorsDoesNotExistForLocation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationSectorService {
    void saveAll(List<LocationSector> locationSectors);
    List<LocationSector> get(Long locationId) throws LocationSectorsDoesNotExistForLocation;
}
