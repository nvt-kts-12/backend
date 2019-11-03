package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
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
                locationSchemeRepository.findByNameIgnoreCase(locationScheme.getName()).isPresent()){
            // because id is null i know he is trying to save new scheme with existing name
            throw new LocationSchemeAlreadyExists(locationScheme.getName());
        }
        // if id is not null we are saving new scheme and if name exists it is update action
        return locationSchemeRepository.save(locationScheme);
    }

    public LocationScheme get(Long id) throws LocationSchemeDoesNotExist {
        LocationScheme location = locationSchemeRepository.findById(id).orElse(null);

        if(location == null || location.isDeleted()){
            throw new LocationSchemeDoesNotExist(id);
        }
        return location;
    }

    public List<LocationScheme> getAll() {
        return locationSchemeRepository.findAll();
    }
}
