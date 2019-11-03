package nvt.kts.ticketapp.service.sector;

import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectorServiceImpl implements SectorService {

    private final SectorRepository sectorRepository;

    public SectorServiceImpl(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }


    public void saveAll(List<SectorDTO> sectorDTOs, LocationScheme locationScheme) {
        List<Sector> sectors = ObjectMapperUtils.mapAll(sectorDTOs, Sector.class);
        for (Sector sector: sectors) {
            sector.setLocationScheme(locationScheme);
            sectorRepository.save(sector);
        }
    }

    public List<SectorDTO> getAll() {
        return ObjectMapperUtils.mapAll(sectorRepository.findAllByDeletedFalse(), SectorDTO.class);
    }

    public SectorDTO get(Long id) throws SectorDoesNotExist {
        Sector sector = sectorRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new SectorDoesNotExist());

        return ObjectMapperUtils.map(sector, SectorDTO.class);
    }

    public List<SectorDTO> getByScheme(Long schemeId) {
        List<Sector> sectors = sectorRepository.findAllByLocationSchemeIdAndDeletedFalse(schemeId);
        return ObjectMapperUtils.mapAll(sectors, SectorDTO.class);
    }

    public Sector getSector(Long id) throws SectorDoesNotExist{
        Sector sector = sectorRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new SectorDoesNotExist());

        return sector;
    }
}
