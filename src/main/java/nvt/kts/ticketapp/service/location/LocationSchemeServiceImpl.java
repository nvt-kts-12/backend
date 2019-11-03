package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationSchemeServiceImpl implements LocationSchemeService {

    private LocationSchemeRepository locationSchemeRepository;

    public LocationSchemeServiceImpl(LocationSchemeRepository locationSchemeRepository){
        this.locationSchemeRepository = locationSchemeRepository;
    }

    public LocationScheme save(LocationScheme locationScheme) throws LocationSchemeAlreadyExists {
        if(locationScheme.getId() == null &&
                locationSchemeRepository.findByNameIgnoreCaseAndDeletedFalse(locationScheme.getName()).isPresent()){
            // because id is null i know he is trying to save new scheme with existing name
            throw new LocationSchemeAlreadyExists(locationScheme.getName());
        }
        // if id is not null we are saving new scheme and if name exists it is update action
        return locationSchemeRepository.save(locationScheme);
    }

    public LocationSchemeDTO get(Long id) throws LocationSchemeDoesNotExist {
        LocationScheme location = locationSchemeRepository.findByIdAndDeletedFalse(id).
                                    orElseThrow(() -> new LocationSchemeDoesNotExist(id));

        return ObjectMapperUtils.map(location, LocationSchemeDTO.class);
    }

    public List<LocationSchemeDTO> getAll() {
        return ObjectMapperUtils.mapAll(locationSchemeRepository.findAllByDeletedFalse(), LocationSchemeDTO.class);

    }

    public LocationScheme getScheme(Long id) throws LocationSchemeDoesNotExist {
        LocationScheme location = locationSchemeRepository.findByIdAndDeletedFalse(id).
                orElseThrow(() -> new LocationSchemeDoesNotExist(id));

        return location;
    }
}
