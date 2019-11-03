package nvt.kts.ticketapp.controller.location;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeSectorsDTO;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.exception.location.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import nvt.kts.ticketapp.service.location.LocationSchemeServiceImpl;
import nvt.kts.ticketapp.service.location.SectorService;
import nvt.kts.ticketapp.service.location.SectorServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Deals with requests that are matter of both location scheme and sectors.
 * It's methods ares used to perform saving and updating location scheme and its sectors or providing all sectors of one schema.
 */
@RestController
@RequestMapping("/api/locationSchemeSector")
public class LocationSchemeSectorController {


    private LocationSchemeService locationSchemeService;
    private SectorService sectorService;

    public LocationSchemeSectorController(LocationSchemeServiceImpl locationSchemeService, SectorServiceImpl sectorService){
        this.locationSchemeService = locationSchemeService;
        this.sectorService = sectorService;
    }

    @PostMapping
    public void save(@RequestBody LocationSchemeSectorsDTO locationSchemeSectorsDTO){
        try {
            sectorService.saveAll(locationSchemeSectorsDTO.getSectors(),
                    locationSchemeService.save(locationSchemeSectorsDTO.getLocationScheme()));
        } catch (LocationSchemeAlreadyExists locationSchemeAlreadyExists) {
            locationSchemeAlreadyExists.printStackTrace();
        }
    }

    @GetMapping("/{schemeId}")
    public List<SectorDTO> getByScheme(@PathVariable Long schemeId){
        return sectorService.getByScheme(schemeId);
    }
}
