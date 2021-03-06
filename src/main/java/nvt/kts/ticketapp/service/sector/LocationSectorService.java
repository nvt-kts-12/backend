package nvt.kts.ticketapp.service.sector;

import nvt.kts.ticketapp.domain.dto.event.LocationSectorsDTO;
import nvt.kts.ticketapp.domain.dto.location.LocationSectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.exception.location.LocationNotFound;
import nvt.kts.ticketapp.exception.location.LocationSectorsDoesNotExistForLocation;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import nvt.kts.ticketapp.exception.sector.LocationSectorDoesNotExist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationSectorService {
    /**
     * Method saves all location sectors from passed list
     *
     * @param locationSectors
     */
    List<LocationSectorDTO> saveAll(List<LocationSectorDTO> locationSectors) throws LocationNotFound, SectorNotFound;

    /**
     * Method provides all location sectors that are assigned to location with passed id
     *
     * @param locationId
     * @return
     * @throws LocationSectorsDoesNotExistForLocation
     */
    List<LocationSector> get(Long locationId) throws LocationSectorsDoesNotExistForLocation;

    /**
     * Provides location sector with given id
     *
     * @param sectorId
     * @return
     * @throws LocationSectorDoesNotExist
     */
    LocationSectorDTO getOne(Long sectorId) throws LocationSectorDoesNotExist;
}
