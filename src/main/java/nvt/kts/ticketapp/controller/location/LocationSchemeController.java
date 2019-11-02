package nvt.kts.ticketapp.controller.location;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeSectorsDTO;
import nvt.kts.ticketapp.exception.location.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.exception.location.LocationSchemeNotFound;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import nvt.kts.ticketapp.service.location.LocationSchemeServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locationScheme")
public class LocationSchemeController {

    private LocationSchemeService locationSchemeService;

    public LocationSchemeController(LocationSchemeServiceImpl locationSchemeService){
        this.locationSchemeService = locationSchemeService;
    }

    @PostMapping
    public void save(@RequestBody LocationSchemeSectorsDTO locationSchemeSectorsDTO){
        try {
            locationSchemeService.save(locationSchemeSectorsDTO.getLocationScheme(), locationSchemeSectorsDTO.getSectors());
        } catch (LocationSchemeAlreadyExists locationSchemeAlreadyExists) {
            locationSchemeAlreadyExists.printStackTrace();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id){
        try {
            return new ResponseEntity<LocationSchemeSectorsDTO>(locationSchemeService.get(id), HttpStatus.OK);
        } catch (LocationSchemeNotFound locationSchemeNotFound) {
            locationSchemeNotFound.printStackTrace();
            return new ResponseEntity<String>(locationSchemeNotFound.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
