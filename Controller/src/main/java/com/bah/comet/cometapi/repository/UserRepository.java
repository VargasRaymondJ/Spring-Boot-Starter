package com.bah.comet.cometapi.repository;

import com.bah.comet.cometapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

// Repo to go along with testing of the database integration
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    /**
     * Gets the non-deleted user that has the unique username/email provided.
     *
     * @param email unique username
     * @return User object if it exists.
     */
    Optional<User> findByEmailIgnoreCaseAndDeletedIsNull(String email);

    /**
     * Gets all non-deleted users.
     * By overriding this method, we filter all user results to exclude deleted users.
     *
     * @return list of users whose deleted date is null.
     */
    @Override
    @Query("select u from #{#entityName} u where u.deleted is null")
    Iterable<User> findAll();

    /**
     * Gets all non-deleted users with pagination and sorting filters.
     * By overriding this method, we filter all user results to exclude deleted users.
     *
     * @return list of users whose deleted date is null.
     */
    @Override
    @Query("select u from #{#entityName} u where u.deleted is null")
    Page<User> findAll(Pageable pageable);

    /**
     * Gets a list of users matching any of the provided emails.
     *
     * @param emails list of unique emails/usernames
     * @return list of Users
     */
    Iterable<User> findByEmailIn(List<String> emails);

}
