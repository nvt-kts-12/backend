package nvt.kts.ticketapp.service.sector;

import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import nvt.kts.ticketapp.exception.sector.CanNotDeleteSchemeSectors;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.repository.sector.LocationSectorRepository;
import nvt.kts.ticketapp.repository.sector.SectorRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorServiceImpl implements SectorService {

    private SectorRepository sectorRepository;
    private LocationSectorRepository locationSectorRepository;

    public SectorServiceImpl(SectorRepository sectorRepository, LocationSectorRepository locationSectorRepository) {
        this.sectorRepository = sectorRepository;
        this.locationSectorRepository = locationSectorRepository;
    }


    public void saveAll(List<SectorDTO> sectorDTOs, LocationScheme locationScheme) {
        List<Sector> sectors = ObjectMapperUtils.mapAll(sectorDTOs, Sector.class);
        for (Sector sector : sectors) {
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

    public Sector getSector(Long id) throws SectorDoesNotExist {
        Sector sector = sectorRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new SectorDoesNotExist());
        return sector;
    }

    public List<SectorDTO> delete(List<SectorDTO> sectorDTOS) throws CanNotDeleteSchemeSectors {
        List<Sector> sectors = ObjectMapperUtils.mapAll(sectorDTOS, Sector.class);

        for (Sector sector : sectors) {
            List<LocationSector> locationSectors = locationSectorRepository.
                    findAllBySectorId(sector.getId());
            if (!locationSectors.isEmpty()) {
                throw new CanNotDeleteSchemeSectors(sector.getId());
            }
            sector.setDeleted(true);
        }

        return ObjectMapperUtils.mapAll(sectorRepository.saveAll(sectors), SectorDTO.class);
    }
}
