package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.model.location.Location;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationService {
    void saveAll(List<Location> locations);
}
