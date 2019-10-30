package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.model.location.Location;

import java.util.List;

public interface LocationService {

    /**
     * Saves location in database
     * @param location  - location to be saved or updated
     */
    public void save(Location location);

    /**
     * Provides one location based on given id
     * @param id    -   location id
     * @return
     */
    public Location getOne(Long id);

    /**
     * Provides list of all locations
     * @return
     */
    public List<Location> getAll(int page, int size);
}
