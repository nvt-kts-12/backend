package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.location.LocationDTO;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.util.LocationMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {

    private LocationRepository locationRepository;

    private ModelMapper modelMapper = new ModelMapper();
    private LocationMapper locationMapper = new LocationMapper();

    public LocationServiceImpl(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    public void save(LocationDTO location) {
        //todo
    }

    public LocationDTO getOne(Long id) {
        Location location = locationRepository.findById(id).orElse(new Location());
        return locationMapper.entityToDto(location.get());
    }

    public List<LocationDTO> get(int page, int size) {
        Page<Location> locations = locationRepository.findAll(PageRequest.of(page, size));
        return locationMapper.entitiesToDtos(locations.getContent());
    }

    public void delete(Long id) {
        Location location = locationRepository.findById(id).orElse(new Location());

        location.setDeleted(true);
        locationRepository.save(location);
    }


}
