package com.pulsar.repository;

import com.pulsar.models.Verification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerificationRepository extends MongoRepository<Verification, String> {

}
