package org.training.user.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.user.service.model.entity.User;

import java.util.Optional;

/**
 * Repository interface for User entity persistence operations.
 * 
 * <p>This interface extends JpaRepository to provide CRUD operations for User entities.
 * It also defines custom query methods for finding users by specific criteria.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.user.service.model.entity.User
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their authentication ID.
     *
     * @param  authId  the authentication ID of the user
     * @return         an optional containing the user if found, otherwise empty
     */
    Optional<User> findUserByAuthId(String authId);
}
