package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeSectorsDTO;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
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
     * @param sectors - sectors in that scheme
     */
    public void save(LocationScheme locationScheme, List<SectorDTO> sectors) throws LocationSchemeAlreadyExists;

    public LocationSchemeSectorsDTO get(Long id) throws LocationSchemeNotFound;
}
