package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.location.SectorDTO;
import nvt.kts.ticketapp.domain.model.location.LocationScheme;
import nvt.kts.ticketapp.repository.location.LocationSchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationSchemeServiceImpl implements LocationSchemeService {

    private LocationSchemeRepository locationSchemeRepository;

    public LocationSchemeServiceImpl(LocationSchemeRepository locationSchemeRepository){
        this.locationSchemeRepository = locationSchemeRepository;
    }

    public void save(LocationScheme locationScheme, List<SectorDTO> sectors) {
        //todo
    }
}
