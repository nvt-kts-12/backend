package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SectorService {

    /**
     * Saves sectors for given scheme
     * @param sectors   -   list of sectors
     * @param locationScheme  -   scheme
     */
    public void saveAll(List<SectorDTO> sectorDTOs, LocationScheme locationScheme);

    /**
     * Provides list of all sectors in database
     * @return
     */
    public List<SectorDTO> getAll();

    /**
     * Provides sector with given id
     * @param id - sector id
     * @return
     */
    public SectorDTO get(Long id) throws SectorNotFound;

    /**
     * Provides sectors of scheme with given id
     * @param schemeId  -   id of a scheme
     * @return
     */
    public List<SectorDTO> getByScheme(Long schemeId);
}
