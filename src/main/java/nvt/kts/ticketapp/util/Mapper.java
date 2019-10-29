package nvt.kts.ticketapp.util;

import java.util.List;

public interface Mapper<Entity, Dto> {
    /**
     * Converts Entity object to dto
     * @param entity
     * @return
     */
    Dto entityToDto(Entity entity);

    /**
     * Converts dto to Entity object
     * @param dto
     * @return
     */
    Entity dtoToEntity(Dto dto);

    /**
     * Converts collection of entities to collection of dtos
     * @param entities
     * @return
     */
    List<Dto> entitiesToDtos(List<Entity> entities);

    /**
     * Converts collection of dtos to collection of entities
     * @param dtos
     * @return
     */
    List<Entity> dtosToEntities(List<Dto> dtos);
}
