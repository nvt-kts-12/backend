package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import nvt.kts.ticketapp.repository.location.SectorRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorServiceImpl implements SectorService {

    private SectorRepository sectorRepository;

    public SectorServiceImpl(SectorRepository sectorRepository){
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
        return ObjectMapperUtils.mapAll(sectorRepository.findAll(), SectorDTO.class);
    }

    public SectorDTO get(Long id) throws SectorNotFound {
        Sector sector = sectorRepository.findById(id).orElse(null);
        if(sector == null) throw new SectorNotFound(id);

        return ObjectMapperUtils.map(sector, SectorDTO.class);
    }

    public List<SectorDTO> getByScheme(Long schemeId) {
        List<Sector> sectors = sectorRepository.findAllByLocationSchemeId(schemeId);
        return ObjectMapperUtils.mapAll(sectors, SectorDTO.class);
    }
}
