package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeCanNotBeDeleted;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationSchemeServiceImpl implements LocationSchemeService {

    private LocationSchemeRepository locationSchemeRepository;
    private LocationRepository locationRepository;

    public LocationSchemeServiceImpl(LocationSchemeRepository locationSchemeRepository,
                                     LocationRepository locationRepository) {
        this.locationSchemeRepository = locationSchemeRepository;
        this.locationRepository = locationRepository;
    }

    public LocationSchemeDTO save(LocationScheme locationScheme) throws LocationSchemeAlreadyExists {
        if (locationScheme.getId() == null &&
                locationSchemeRepository.findByNameIgnoreCaseAndDeletedFalse(locationScheme.getName()).isPresent()) {
            // because id is null i know he is trying to save new scheme with existing name
            throw new LocationSchemeAlreadyExists(locationScheme.getName());
        }
        // if id is not null it is update action
        return ObjectMapperUtils.map(locationSchemeRepository.save(locationScheme), LocationSchemeDTO.class);
    }

    public LocationSchemeDTO get(Long id) throws LocationSchemeDoesNotExist {
        LocationScheme location = locationSchemeRepository.findByIdAndDeletedFalse(id).
                orElseThrow(() -> new LocationSchemeDoesNotExist(id));

        return ObjectMapperUtils.map(location, LocationSchemeDTO.class);
    }

    public List<LocationSchemeDTO> getAll() {
        return ObjectMapperUtils.mapAll(locationSchemeRepository.findAllByDeletedFalse(), LocationSchemeDTO.class);
    }

    public LocationSchemeDTO delete(Long id) throws LocationSchemeDoesNotExist, LocationSchemeCanNotBeDeleted {
        LocationScheme locationScheme = locationSchemeRepository.findByIdAndDeletedFalse(id).
                orElseThrow(() -> new LocationSchemeDoesNotExist(id));

        List<Location> locations = locationRepository.findAllBySchemeIdAndDeletedFalse(locationScheme.getId());

        if (!locations.isEmpty()) {
            throw new LocationSchemeCanNotBeDeleted(id);
        }

        locationScheme.setDeleted(true);
        return ObjectMapperUtils.map(locationSchemeRepository.save(locationScheme), LocationSchemeDTO.class);
    }


    public LocationScheme getScheme(Long id) throws LocationSchemeDoesNotExist {
        LocationScheme location = locationSchemeRepository.findByIdAndDeletedFalse(id).
                orElseThrow(() -> new LocationSchemeDoesNotExist(id));

        return location;
    }
}
