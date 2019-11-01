package nvt.kts.ticketapp.controller.location;

import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.service.location.LocationSchemeService;
import nvt.kts.ticketapp.service.location.LocationSchemeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LocationSchemeController {

    @Autowired
    private LocationSchemeService locationSchemeService;


    @PostMapping
    public void save(LocationScheme locationScheme, List<SectorDTO> sectors){
        locationSchemeService.save(locationScheme, sectors);
    }
}
