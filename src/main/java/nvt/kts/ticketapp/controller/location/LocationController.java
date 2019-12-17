package nvt.kts.ticketapp.controller.location;

import nvt.kts.ticketapp.domain.dto.event.LocationDTO;
import nvt.kts.ticketapp.domain.dto.location.PlainLocationDTO;
import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.service.location.LocationService;
import nvt.kts.ticketapp.service.location.LocationServiceImpl;
import nvt.kts.ticketapp.service.sector.LocationSectorService;
import nvt.kts.ticketapp.service.sector.LocationSectorServiceImpl;
import nvt.kts.ticketapp.service.sector.SectorService;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Deals with requests that are matter of location.
 */
@RestController
@RequestMapping("/api/location")
public class LocationController {

    private LocationService locationService;
    private LocationSectorService locationSectorService;

    public LocationController(LocationServiceImpl locationService, LocationSectorServiceImpl locationSectorService){
        this.locationService = locationService;
        this.locationSectorService = locationSectorService;
    }

    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity save(@RequestBody @Valid PlainLocationDTO locationDTO){
        try {
            return new ResponseEntity<PlainLocationDTO>(locationService.save(locationDTO.getLocationSchemeId()), HttpStatus.OK);
        } catch (LocationSchemeDoesNotExist locationSchemeDoesNotExist) {
            locationSchemeDoesNotExist.printStackTrace();
            return new ResponseEntity<String>("Location scheme with such id does not exist.", HttpStatus.BAD_REQUEST);
        }
    }
}
