package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeNotExist;
import nvt.kts.ticketapp.repository.locationScheme.LocationSchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationSchemeServiceImpl implements LocationSchemeService{

    private final LocationSchemeRepository locationSchemeRepository;

    @Autowired
    public LocationSchemeServiceImpl(LocationSchemeRepository locationSchemeRepository) {
        this.locationSchemeRepository = locationSchemeRepository;
    }

    @Override
    public LocationScheme findOneById(Long locationSchemeId) throws LocationSchemeNotExist {

        Optional<LocationScheme> locationScheme = locationSchemeRepository.findOneById(locationSchemeId);

        if (locationScheme.isEmpty()) {
            throw new LocationSchemeNotExist();
        }

        return locationScheme.get();
    }
}
