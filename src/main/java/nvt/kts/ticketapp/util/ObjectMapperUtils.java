package nvt.kts.ticketapp.util;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class with static methods for object mapping.
 *
 */
public class ObjectMapperUtils {

    private static ModelMapper modelMapper = new ModelMapper();

    /**
     * Hide from public usage.
     */
    private ObjectMapperUtils() {
    }

    /**
     * <p>Note: outClass object must have default constructor with no arguments</p>
     *
     * @param <D>      type of result object.
     * @param <T>      type of source object to map from.
     * @param object   object that needs to be mapped.
     * @param outClass class of result object.
     * @return new object of <code>outClass</code> type.
     */
    public static <D, T> D map(final T object, Class<D> outClass) {
        return modelMapper.map(object, outClass);
    }

    /**
     * <p>Note: outClass object must have default constructor with no arguments</p>
     *
     * @param objectList list of objects that needs to be mapped
     * @param outCLass   class of result list element
     * @param <D>        type of objects in result list
     * @param <T>        type of object in <code>objectList</code>
     * @return list of mapped object with <code><D></code> type.
     */
    public static <D, T> List<D> mapAll(final Collection<T> objectList, Class<D> outCLass) {
        return objectList.stream()
                .map(entity -> map(entity, outCLass))
                .collect(Collectors.toList());
    }
}
