package nvt.kts.ticketapp.service.sector;

import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import org.springframework.stereotype.Service;

@Service
public interface SectorService  {
    Sector findOneById(Long sectorId) throws SectorDoesNotExist;
}
