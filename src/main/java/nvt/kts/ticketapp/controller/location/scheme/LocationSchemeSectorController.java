package nvt.kts.ticketapp.controller.location.scheme;

import nvt.kts.ticketapp.domain.dto.location.LocationSchemeDTO;
import nvt.kts.ticketapp.domain.dto.location.LocationSchemeSectorsDTO;
import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeCanNotBeDeleted;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeAlreadyExists;
import nvt.kts.ticketapp.exception.locationScheme.LocationSchemeDoesNotExist;
import nvt.kts.ticketapp.exception.sector.CanNotDeleteSchemeSectors;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import nvt.kts.ticketapp.service.location.LocationSchemeServiceImpl;
import nvt.kts.ticketapp.service.sector.SectorService;
import nvt.kts.ticketapp.service.sector.SectorServiceImpl;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    public LocationSchemeSectorController(LocationSchemeService locationSchemeService, SectorService sectorService) {
        this.locationSchemeService = locationSchemeService;
        this.sectorService = sectorService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity save(@RequestBody @Valid LocationSchemeSectorsDTO locationSchemeSectorsDTO){
        try {
            LocationSchemeDTO savedScheme = locationSchemeService.
                    save(ObjectMapperUtils.map(locationSchemeSectorsDTO.getLocationScheme(), LocationScheme.class));
            List<SectorDTO> savedSectors = sectorService.saveAll(locationSchemeSectorsDTO.getSectors(),
                    ObjectMapperUtils.map(savedScheme, LocationScheme.class));

            return new ResponseEntity<LocationSchemeSectorsDTO>(
                    new LocationSchemeSectorsDTO(savedSectors, savedScheme),HttpStatus.OK);
        } catch (LocationSchemeAlreadyExists locationSchemeAlreadyExists) {
            locationSchemeAlreadyExists.printStackTrace();
            return new ResponseEntity<String>(locationSchemeAlreadyExists.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{schemeId}")
    public ResponseEntity getByScheme(@PathVariable Long schemeId) {
        return new ResponseEntity<List<SectorDTO>>(sectorService.getByScheme(schemeId), HttpStatus.OK);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity delete(@RequestBody @Valid LocationSchemeSectorsDTO locationSchemeSectorsDTO){
        try {
            List<SectorDTO> sectorDTOS = sectorService.delete(locationSchemeSectorsDTO.getSectors());
            LocationSchemeDTO locationSchemeDTO = locationSchemeService.delete(locationSchemeSectorsDTO.getLocationScheme().getId());

            return new ResponseEntity<LocationSchemeSectorsDTO>(
                    new LocationSchemeSectorsDTO(sectorDTOS, locationSchemeDTO), HttpStatus.OK);

        } catch (CanNotDeleteSchemeSectors | LocationSchemeDoesNotExist | LocationSchemeCanNotBeDeleted ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
