package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.location.LocationDTO;


import java.util.List;

public interface LocationService {

    /**
     * Saves location in database
     * @param location  - location to be saved or updated
     */
    public void save(LocationDTO locationDTO);

    /**
     * Provides one location based on given id
     * @param id    -   location id
     * @return
     */
    public LocationDTO getOne(Long id);

    /**
     * Provides list of all locations
     * @return
     */
    public List<LocationDTO> get(int page, int size);

    /**
     * Removes location with given id
     * @param id
     */
    public void delete(Long id);
}
