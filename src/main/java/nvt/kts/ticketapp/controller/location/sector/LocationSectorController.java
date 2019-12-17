package nvt.kts.ticketapp.controller.location.sector;

import nvt.kts.ticketapp.domain.dto.event.LocationSectorsDTO;
import nvt.kts.ticketapp.domain.dto.location.LocationSectorDTO;
import nvt.kts.ticketapp.domain.dto.location.LocationSectorsWrapperDTO;
import nvt.kts.ticketapp.domain.model.location.LocationSector;
import nvt.kts.ticketapp.exception.location.LocationNotFound;
import nvt.kts.ticketapp.exception.location.SectorNotFound;
import nvt.kts.ticketapp.service.sector.LocationSectorService;
import nvt.kts.ticketapp.service.sector.LocationSectorServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/locationSector")
public class LocationSectorController {

    private LocationSectorService locationSectorService;

    public LocationSectorController(LocationSectorServiceImpl locationSectorService) {
        this.locationSectorService = locationSectorService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity saveAll(@RequestBody @Valid LocationSectorsWrapperDTO locationSectorsWrapperDTO) {
        try {
            return new ResponseEntity<List<LocationSectorDTO>>
                    (locationSectorService.saveAll(locationSectorsWrapperDTO.getSectors()), HttpStatus.OK);
        } catch (LocationNotFound | SectorNotFound locationNotFound) {
            locationNotFound.printStackTrace();
            return new ResponseEntity<String>("Location or sector with such id does not exist", HttpStatus.BAD_REQUEST);
        }
    }
}
