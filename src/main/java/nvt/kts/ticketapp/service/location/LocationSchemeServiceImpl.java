package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeSectorsDTO;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.exception.location.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.exception.location.LocationSchemeNotFound;
import nvt.kts.ticketapp.repository.location.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.location.SectorRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationSchemeServiceImpl implements LocationSchemeService {

    private LocationSchemeRepository locationSchemeRepository;
    private SectorRepository sectorRepository;

    public LocationSchemeServiceImpl(LocationSchemeRepository locationSchemeRepository, SectorRepository sectorRepository){
        this.locationSchemeRepository = locationSchemeRepository;
        this.sectorRepository = sectorRepository;
    }

    public LocationScheme save(LocationScheme locationScheme) throws LocationSchemeAlreadyExists {
        if(locationScheme.getId() == null &&
                locationSchemeRepository.findByNameIgnoreCase(locationScheme.getName()).isPresent()){
            // because id is null i know he is trying to save new scheme with existing name
            throw new LocationSchemeAlreadyExists(locationScheme.getName());
        }
        // if id is not null we are saving new scheme and if name exists it is update action
        return locationSchemeRepository.save(locationScheme);
    }

    public LocationScheme get(Long id) throws LocationSchemeNotFound {
        LocationScheme location = locationSchemeRepository.findById(id).orElse(null);

        if(location == null || location.isDeleted()){
            throw new LocationSchemeNotFound(id);
        }
        return location;
    }

    public List<LocationScheme> getAll() {
        return locationSchemeRepository.findAll();
    }
}
