package nvt.kts.ticketapp.controller;

import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.service.location.LocationService;
import nvt.kts.ticketapp.service.location.LocationServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location")
public class LocationController {

    private LocationService locationService;

    public LocationController(LocationServiceImpl locationService){
        locationService = locationService;
    }

    public void save(Location location){
        locationService.save(location);
    }
}
