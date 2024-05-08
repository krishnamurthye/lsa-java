package com.pulsar.repository;

import com.pulsar.models.LSAPProfile;
import com.pulsar.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileRepository extends MongoRepository<LSAPProfile, String> {

    Optional<LSAPProfile> findByUser(User user);

    Optional<LSAPProfile> findById(String id);
}
