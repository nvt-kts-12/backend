package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.model.location.Location;

public interface LocationService {

    /**
     * Saves location in database
     * @param location  - location to be saved or updated
     */
    public void save(Location location);
}
