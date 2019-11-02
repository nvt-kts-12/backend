package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationSchemeServiceImpl implements LocationSchemeService{

    private final LocationSchemeRepository locationSchemeRepository;

    public LocationSchemeServiceImpl(LocationSchemeRepository locationSchemeRepository) {
        this.locationSchemeRepository = locationSchemeRepository;
    }

    @Override
    public LocationScheme findOneById(Long locationSchemeId) throws LocationSchemeDoesNotExist {

        Optional<LocationScheme> locationScheme = locationSchemeRepository.findOneById(locationSchemeId);

        if (locationScheme.isEmpty()) {
            throw new LocationSchemeDoesNotExist();
        }

        return locationScheme.get();
    }
}
