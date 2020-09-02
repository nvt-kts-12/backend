package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeCanNotBeDeleted;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationSchemeService {

    /**
     * Saves location scheme in database.
     * @param locationScheme - scheme to save
     * @return
     */
    LocationSchemeDTO save(LocationScheme locationScheme) throws LocationSchemeAlreadyExists;

    /**
     * Provides location scheme with given id
     * @param id
     * @return  -   returns location scheme with sectors provided
     * @throws LocationSchemeDoesNotExist
     */
    LocationSchemeDTO get(Long id) throws LocationSchemeDoesNotExist;

    /**
     * Provides all location schemes
     * @return
     */
    List<LocationSchemeDTO> getAll();

    /**
     * Provides location scheme Entity by its id
     * @param id    -   id of scheme that is searched for
     * @return
     * @throws LocationSchemeDoesNotExist
     */
    LocationScheme getScheme(Long id) throws LocationSchemeDoesNotExist;

    /**
     * Deletes location scheme from database
     * @param id    -   id of scheme that is going to be deleted
     * @throws LocationSchemeDoesNotExist
     */
    LocationSchemeDTO delete(Long id) throws LocationSchemeDoesNotExist, LocationSchemeCanNotBeDeleted;
}
