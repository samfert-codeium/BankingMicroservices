package org.training.user.service.model.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Abstract base class for entity-DTO mapping operations.
 * 
 * <p>This class provides a generic framework for converting between entity objects
 * and their corresponding Data Transfer Objects (DTOs). Concrete implementations
 * must provide the specific conversion logic for their entity-DTO pairs.</p>
 * 
 * @param <E> the entity type
 * @param <D> the DTO type
 * @author Training Team
 * @version 1.0
 */
public abstract class BaseMapper<E, D> {

    /**
     * Converts a DTO to its corresponding entity.
     * 
     * @param dto the DTO to convert
     * @param args optional additional arguments for conversion
     * @return the converted entity
     */
    public abstract E convertToEntity(D dto, Object... args);

    /**
     * Converts an entity to its corresponding DTO.
     * 
     * @param entity the entity to convert
     * @param args optional additional arguments for conversion
     * @return the converted DTO
     */
    public abstract D convertToDto(E entity, Object... args);

    public Collection<E> convertToEntity(Collection<D> dto, Object... args) {
        return dto.stream().map(d -> convertToEntity(d, args)).collect(Collectors.toList());
    }

    public Collection<D> convertToDto(Collection<E> entities, Object... args) {
        return entities.stream().map(entity ->convertToDto(entity, args)).collect(Collectors.toList());
    }

    public List<E> covertToEntityList(Collection<D> dtos, Object... args) {
        return convertToEntity(dtos, args).stream().collect(Collectors.toList());
    }

    public List<D> convertToDtoList(Collection<E> entities, Object... args) {
        return convertToDto(entities, args).stream().collect(Collectors.toList());
    }

    public Set<E> convertToEntitySet(Collection<D> dtos, Object... args) {
        return convertToEntity(dtos, args).stream().collect(Collectors.toSet());
    }

    public Set<D> convertToDtoSet(Collection<E> entities, Object... args) {
        return convertToDto(entities, args).stream().collect(Collectors.toSet());
    }

}
