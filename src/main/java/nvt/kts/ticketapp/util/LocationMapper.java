package nvt.kts.ticketapp.util;

import nvt.kts.ticketapp.domain.dto.location.LocationDTO;
import nvt.kts.ticketapp.domain.model.location.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationMapper implements Mapper<Location, LocationDTO> {

    public LocationDTO entityToDto(Location location) {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setLocationScheme(location.getScheme());
        return locationDTO;
    }

    public Location dtoToEntity(LocationDTO locationDTO) {
        return null;
    }

    @Override
    public List<LocationDTO> entitiesToDtos(List<Location> locations) {
        List<LocationDTO> locationDTOS = new ArrayList<>();
        for (Location location: locations) {
            LocationDTO locationDTO = entityToDto(location);
            locationDTOS.add(locationDTO);
        }
        return locationDTOS;
    }

    @Override
    public List dtosToEntities(List list) {
        return null;
    }
}
