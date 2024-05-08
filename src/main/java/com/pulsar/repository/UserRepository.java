package com.pulsar.repository;

import com.pulsar.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(String id);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.userType = :userType")
    Optional<User> findByIdUserType(@Param("id") String id, @Param("userType") int userType);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);


}
