package nvt.kts.ticketapp.service.location;

import nvt.kts.ticketapp.domain.dto.location.LocationDTO;
import nvt.kts.ticketapp.domain.model.location.Location;
import nvt.kts.ticketapp.exception.location.LocationNotFound;
import nvt.kts.ticketapp.repository.location.LocationRepository;
import nvt.kts.ticketapp.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    public void save(LocationDTO location) {
        //todo
    }

    public LocationDTO getOne(Long id) throws LocationNotFound {
        Location location = locationRepository.findById(id).orElse(null);
        if(location == null || location.isDeleted()){
            throw new LocationNotFound(id);
        }else{
            return ObjectMapperUtils.map(location, LocationDTO.class);
        }
    }

    public List<LocationDTO> get(int page, int size) {
        Page<Location> locations = locationRepository.findAll(PageRequest.of(page, size));
        return ObjectMapperUtils.mapAll(removeDeletedLocations(locations.getContent()), LocationDTO.class);
    }

    public void delete(Long id) throws LocationNotFound{
        Location location = locationRepository.findById(id).orElse(null);
        if(location == null || location.isDeleted()){
            throw new LocationNotFound(id);
        }else{
            location.setDeleted(true);
            locationRepository.save(location);
        }
    }


    private List<Location> removeDeletedLocations(List<Location> locations){
        List<Location> locationsToReturn = new ArrayList<>();
        for (Location location : locations) {
            if(!location.isDeleted()){
                locationsToReturn.add(location);
            }
        }
        return locationsToReturn;
    }
}
