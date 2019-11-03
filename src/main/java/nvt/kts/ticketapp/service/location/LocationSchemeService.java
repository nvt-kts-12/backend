package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import org.springframework.stereotype.Service;

@Service
public interface LocationSchemeService {
    LocationScheme findOneById(Long locationSchemeId) throws LocationSchemeDoesNotExist;
}
