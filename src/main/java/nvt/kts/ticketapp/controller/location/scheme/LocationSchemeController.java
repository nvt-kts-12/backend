package nvt.kts.ticketapp.controller.location.scheme;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import nvt.kts.ticketapp.service.location.LocationSchemeServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    public LocationSchemeController(LocationSchemeService locationSchemeService) {
        this.locationSchemeService = locationSchemeService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity get(@PathVariable Long id){
        try {
            return new ResponseEntity<LocationSchemeDTO>(locationSchemeService.get(id), HttpStatus.OK);
        } catch (LocationSchemeDoesNotExist locationSchemeDoesNotExist) {
            locationSchemeDoesNotExist.printStackTrace();
            return new ResponseEntity<String>(locationSchemeDoesNotExist.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity getAll(){
        return new ResponseEntity<List<LocationSchemeDTO>>(locationSchemeService.getAll(), HttpStatus.OK);
    }
}
