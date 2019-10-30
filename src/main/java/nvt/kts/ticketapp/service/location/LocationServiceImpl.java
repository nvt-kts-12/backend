package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public void save(Location location) {
        //todo
    }

    public Location getOne(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        return location.get();
    }

    public List<Location> getAll(int page, int size) {
        Page<Location> locations = locationRepository.findAll(PageRequest.of(page, size));
        return (List<Location>) locations;
    }


}
