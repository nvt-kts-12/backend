package nvt.kts.ticketapp.controller.location;

import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.Sector;
import nvt.kts.ticketapp.exception.sector.SectorDoesNotExist;
import nvt.kts.ticketapp.service.sector.SectorService;
import nvt.kts.ticketapp.service.sector.SectorServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Deals with requests that are matter of sectors.
 * Its methods provide all sectors or single sector by sector id
 */
@RestController
@RequestMapping("/api/sector")
public class SectorController {

    private SectorService sectorService;

    public SectorController(SectorService sectorService){
        this.sectorService = sectorService;
    }

    @GetMapping
    public List<SectorDTO> getAll(){
        return sectorService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id){
        try {
            SectorDTO sectorDTO = sectorService.get(id);
            return new ResponseEntity<SectorDTO>(sectorDTO, HttpStatus.OK);
        } catch (SectorDoesNotExist sectorDoesNotExist) {
            sectorDoesNotExist.printStackTrace();
            return new ResponseEntity<String>(sectorDoesNotExist.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
