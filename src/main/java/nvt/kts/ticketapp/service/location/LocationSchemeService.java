package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeSectorsDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.location.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.exception.location.LocationSchemeNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationSchemeService {

    /**
     * Saves location scheme in database.
     * @param locationScheme - scheme to save
     * @return
     */
    public LocationScheme save(LocationScheme locationScheme) throws LocationSchemeAlreadyExists;

    /**
     * Provides location scheme with given id
     * @param id
     * @return  -   returns location scheme with sectors provided
     * @throws LocationSchemeNotFound
     */
    public LocationScheme get(Long id) throws LocationSchemeNotFound;

    /**
     * Provides all location schemes
     * @return
     */
    public List<LocationScheme> getAll();
}
