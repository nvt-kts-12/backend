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
    public SectorDTO get(Long id) throws SectorDoesNotExist;

    /**
     * Provides sectors of scheme with given id
     * @param schemeId  -   id of a scheme
     * @return
     */
    public List<SectorDTO> getByScheme(Long schemeId);

    /**
     * Provides whole sector entity!
     * @param id    -   id of sector to be found
     * @return
     */
    public Sector getSector(Long id) throws SectorDoesNotExist;

    /**
     * Deletes all given sectors from database
     * @param sectorDTOS    -   list of sectors to delete
     */
    public List<SectorDTO> delete(List<SectorDTO> sectorDTOS) throws CanNotDeleteSchemeSectors;
}
