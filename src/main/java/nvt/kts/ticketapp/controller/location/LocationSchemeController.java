package nvt.kts.ticketapp.controller.location;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeSectorsDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.location.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.exception.location.LocationSchemeNotFound;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import nvt.kts.ticketapp.service.location.LocationSchemeServiceImpl;
import nvt.kts.ticketapp.service.location.SectorService;
import nvt.kts.ticketapp.service.location.SectorServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Deals with requests that are matter of location scheme.
 * Its methods are used for providing single scheme or all schemas.
 */
@RestController
@RequestMapping("/api/locationScheme")
public class LocationSchemeController {

    private LocationSchemeService locationSchemeService;

    public LocationSchemeController(LocationSchemeServiceImpl locationSchemeService){
        this.locationSchemeService = locationSchemeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id){
        try {
            return new ResponseEntity<LocationScheme>(locationSchemeService.get(id), HttpStatus.OK);
        } catch (LocationSchemeNotFound locationSchemeNotFound) {
            locationSchemeNotFound.printStackTrace();
            return new ResponseEntity<String>(locationSchemeNotFound.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity getAll(){
        return new ResponseEntity<List<LocationScheme>>(locationSchemeService.getAll(), HttpStatus.OK);
    }
}
