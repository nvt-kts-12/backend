package nvt.kts.ticketapp.service.sector;

import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import nvt.kts.ticketapp.exception.sector.CanNotDeleteSchemeSectors;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SectorService  {

    /**
     * Saves sectors for given scheme
     * @param sectorDTOs   -   list of sectors
     * @param locationScheme  -   scheme
     */
    List<SectorDTO> saveAll(List<SectorDTO> sectorDTOs, LocationScheme locationScheme);

    /**
     * Provides list of all sectors in database
     * @return
     */
    List<SectorDTO> getAll();

    /**
     * Provides sector with given id
     * @param id - sector id
     * @return
     */
    SectorDTO get(Long id) throws SectorDoesNotExist;

    /**
     * Provides sectors of scheme with given id
     * @param schemeId  -   id of a scheme
     * @return
     */
    List<SectorDTO> getByScheme(Long schemeId);

    /**
     * Provides whole sector entity!
     * @param id    -   id of sector to be found
     * @return
     */
    Sector getSector(Long id) throws SectorDoesNotExist;

    /**
     * Deletes all given sectors from database
     * @param sectorDTOS    -   list of sectors to delete
     */
    List<SectorDTO> delete(List<SectorDTO> sectorDTOS) throws CanNotDeleteSchemeSectors;
}
