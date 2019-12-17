package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.event.LocationDTO;
import nvt.kts.ticketapp.domain.dto.location.PlainLocationDTO;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationService {
    /**
     * Method for saving all locations from passed list
     *
     * @param locations
     */
    void saveAll(List<Location> locations);

    /**
     * Method for saving single passed location
     *
     * @param locationSchemeId
     * @return
     */
    PlainLocationDTO save(Long locationSchemeId) throws LocationSchemeDoesNotExist;
}
