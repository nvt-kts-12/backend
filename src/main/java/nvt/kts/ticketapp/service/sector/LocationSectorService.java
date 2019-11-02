package nvt.kts.ticketapp.service.sector;

import nvt.kts.ticketapp.domain.model.location.LocationSector;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationSectorService {
    void saveAll(List<LocationSector> locationSectors);
}
