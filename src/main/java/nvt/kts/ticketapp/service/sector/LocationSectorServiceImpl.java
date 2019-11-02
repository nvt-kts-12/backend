package nvt.kts.ticketapp.service.sector;

import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationSectorServiceImpl implements LocationSectorService {

    private final LocationSectorRepository locationSectorRepository;

    @Autowired
    public LocationSectorServiceImpl(LocationSectorRepository locationSectorRepository) {
        this.locationSectorRepository = locationSectorRepository;
    }

    @Override
    public void saveAll(List<LocationSector> locationSectors) {
        locationSectorRepository.saveAll(locationSectors);
    }
}
