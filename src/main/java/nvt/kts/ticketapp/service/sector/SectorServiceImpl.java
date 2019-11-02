package nvt.kts.ticketapp.service.sector;

import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SectorServiceImpl implements SectorService {

    private final SectorRepository sectorRepository;

    public SectorServiceImpl(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    @Override
    public Sector findOneById(Long sectorId) throws SectorDoesNotExist {

        Optional<Sector> sector = sectorRepository.findOneById(sectorId);

        if (sector.isEmpty()) {
            throw new SectorDoesNotExist();
        }

        return sector.get();
    }
}
