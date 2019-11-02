package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeSectorsDTO;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.exception.location.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.exception.location.LocationSchemeNotFound;
import nvt.kts.ticketapp.repository.location.LocationSchemeRepository;
import nvt.kts.ticketapp.repository.location.SectorRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void save(LocationScheme locationScheme, List<SectorDTO> sectorDTOs) throws LocationSchemeAlreadyExists {
        if(locationScheme.getId() == null &&
                locationSchemeRepository.findByNameIgnoreCase(locationScheme.getName()).isPresent()){
            throw new LocationSchemeAlreadyExists(locationScheme.getName());
        }

        LocationScheme savedScheme = locationSchemeRepository.save(locationScheme);
        List<Sector> sectors = ObjectMapperUtils.mapAll(sectorDTOs, Sector.class);
        for (Sector sector: sectors) {
            sector.setLocationScheme(savedScheme);
            sectorRepository.save(sector);
        }
    }

    public LocationSchemeSectorsDTO get(Long id) throws LocationSchemeNotFound {
        LocationScheme location = locationSchemeRepository.findById(id).orElse(null);

        if(location == null || location.isDeleted()){
            throw new LocationSchemeNotFound(id);
        }
        List<Sector> sectors = sectorRepository.findAllByLocationSchemeId(id);

        List<SectorDTO> sectorDTOS = ObjectMapperUtils.mapAll(sectors, SectorDTO.class);
        return new LocationSchemeSectorsDTO(location, sectorDTOS);
    }
}
