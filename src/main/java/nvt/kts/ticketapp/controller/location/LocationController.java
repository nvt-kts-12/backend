package nvt.kts.ticketapp.controller.location;

import nvt.kts.ticketapp.domain.dto.location.LocationDTO;
import nvt.kts.ticketapp.service.location.LocationService;
import nvt.kts.ticketapp.service.location.LocationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    private LocationService locationService;

    @Autowired
    public LocationController(LocationServiceImpl locationService){
        locationService = locationService;
    }

    @PostMapping("/save")
    public void save(LocationDTO location){
        locationService.save(location);
    }

    @PostMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        locationService.delete(id);
    }

    @GetMapping("/get/{id}")
    public LocationDTO get(@PathVariable Long id){
        return locationService.getOne(id);
    }

    @GetMapping("/getAll")
    public List<LocationDTO> get(int page, int size){
        return locationService.get(page, size);
    }
}
