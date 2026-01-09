package org.training.user.service.model.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.training.user.service.model.dto.UserDto;
import org.training.user.service.model.dto.UserProfileDto;
import org.training.user.service.model.entity.User;
import org.training.user.service.model.entity.UserProfile;

import java.util.Objects;

/**
 * Mapper class for converting between {@link User} entities and {@link UserDto} objects.
 * 
 * <p>This mapper extends {@link BaseMapper} and provides specific implementation
 * for converting user data between the persistence layer (entities) and the
 * API layer (DTOs). It handles nested UserProfile conversion as well.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.user.service.model.mapper.BaseMapper
 */
public class UserMapper extends BaseMapper<User, UserDto>{

    /** ModelMapper instance for object mapping. */
    private final ModelMapper mapper = new ModelMapper();

    /**
     * Converts a {@link UserDto} to a {@link User} entity.
     * 
     * <p>Uses Spring's BeanUtils to copy properties from the DTO to a new entity.
     * Also handles nested UserProfileDto to UserProfile conversion.</p>
     * 
     * @param dto the UserDto to convert
     * @param args optional additional arguments (not used)
     * @return the converted User entity
     */
    @Override
    public User convertToEntity(UserDto dto, Object... args) {

        User user = new User();
        if(!Objects.isNull(dto)){
            BeanUtils.copyProperties(dto, user);
            if(!Objects.isNull(dto.getUserProfileDto())){
                UserProfile userProfile = new UserProfile();
                BeanUtils.copyProperties(dto.getUserProfileDto(), userProfile);
                user.setUserProfile(userProfile);
            }
        }
        return user;
    }

    /**
     * Converts a {@link User} entity to a {@link UserDto}.
     * 
     * <p>Uses Spring's BeanUtils to copy properties from the entity to a new DTO.
     * Also handles nested UserProfile to UserProfileDto conversion.</p>
     * 
     * @param entity the User entity to convert
     * @param args optional additional arguments (not used)
     * @return the converted UserDto
     */
    @Override
    public UserDto convertToDto(User entity, Object... args) {

        UserDto userDto = new UserDto();
        if(!Objects.isNull(entity)){
            BeanUtils.copyProperties(entity, userDto);
            if(!Objects.isNull(entity.getUserProfile())) {
                UserProfileDto userProfileDto = new UserProfileDto();
                BeanUtils.copyProperties(entity.getUserProfile(), userProfileDto);
                userDto.setUserProfileDto(userProfileDto);
            }
        }
        return userDto;
    }
}
