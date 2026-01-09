package org.training.account.service.model.mapper;

import java.util.Collection;
import java.util.List;

/**
 * Abstract base class for mapping between entity and DTO objects.
 * 
 * <p>This generic mapper provides a template for converting between entity objects
 * (used for database persistence) and Data Transfer Objects (used for API communication).
 * Concrete implementations must provide the specific conversion logic.</p>
 * 
 * @param <E> the entity type
 * @param <D> the DTO type
 * 
 * @author Training Team
 * @version 1.0
 */
public abstract class BaseMapper<E, D> {

    /**
     * Converts a DTO to an entity.
     * 
     * @param dto the DTO to convert
     * @param args optional additional arguments for conversion
     * @return the converted entity
     */
    public abstract E convertToEntity(D dto, Object... args);

    /**
     * Converts an entity to a DTO.
     * 
     * @param entity the entity to convert
     * @param args optional additional arguments for conversion
     * @return the converted DTO
     */
    public abstract D convertToDto(E entity, Object... args);

    /**
     * Converts a collection of DTOs to a collection of entities.
     * 
     * @param dto the collection of DTOs to convert
     * @param args optional additional arguments for conversion
     * @return a collection of converted entities
     */
    public Collection<E> convertToEntity(Collection<D> dto, Object... args) {
        return dto.stream().map(d -> convertToEntity(d, args)).toList();
    }

    /**
     * Converts a collection of entities to a collection of DTOs.
     * 
     * @param entities the collection of entities to convert
     * @param args optional additional arguments for conversion
     * @return a collection of converted DTOs
     */
    public Collection<D> convertToDto(Collection<E> entities, Object... args) {
        return entities.stream().map(entity -> convertToDto(entity, args)).toList();
    }

    /**
     * Converts a collection of DTOs to a list of entities.
     * 
     * @param dto the collection of DTOs to convert
     * @param args optional additional arguments for conversion
     * @return a list of converted entities
     */
    public List<E> convertToEntityList(Collection<D> dto, Object... args) {
        return convertToEntity(dto, args).stream().toList();
    }

    /**
     * Converts a collection of entities to a list of DTOs.
     * 
     * @param entities the collection of entities to convert
     * @param args optional additional arguments for conversion
     * @return a list of converted DTOs
     */
    public List<D> convertToDtoList(Collection<E> entities, Object... args) {
        return convertToDto(entities, args).stream().toList();
    }
}
