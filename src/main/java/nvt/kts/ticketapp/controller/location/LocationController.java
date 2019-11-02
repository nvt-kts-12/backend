package nvt.kts.ticketapp.controller.location;

import nvt.kts.ticketapp.domain.dto.location.LocationDTO;
import nvt.kts.ticketapp.exception.location.LocationNotFound;
import nvt.kts.ticketapp.service.location.LocationService;
import nvt.kts.ticketapp.service.location.LocationServiceImpl;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    private LocationService locationService;

    public LocationController(LocationServiceImpl locationService){
        this.locationService = locationService;
    }

    @PostMapping("/save")
    public void save(LocationDTO location){
        locationService.save(location);
    }

    @PostMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        try {
            locationService.delete(id);
        } catch (LocationNotFound locationNotFound) {
            locationNotFound.printStackTrace();
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity get(@PathVariable Long id)  {
        LocationDTO locationDTO = null;
        try {
            locationDTO = locationService.getOne(id);
        } catch (LocationNotFound locationNotFound) {
            locationNotFound.printStackTrace();
            return new ResponseEntity<String>(locationNotFound.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<LocationDTO>(locationDTO, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public List<LocationDTO> get(int page, int size){
        return locationService.get(page, size);
    }
}
