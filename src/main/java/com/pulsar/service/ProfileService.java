package com.pulsar.service;

import com.pulsar.payload.request.LSAProfileRequest;
import com.pulsar.payload.request.ProfileRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    ResponseEntity<?> createParentProfile(String loggedInUserId, ProfileRequest profileRequest) throws Exception;

    ResponseEntity<?> updateLSAProfile(String loggedInUserId, LSAProfileRequest request) throws Exception;

    ResponseEntity<String> uploadLSACV(String loggedInUserId, MultipartFile file);

    ResponseEntity<String> uploadEducationalFile(String loggedInUserId, MultipartFile file);

    ResponseEntity<String> uploadOtherFile(String loggedInUserId, MultipartFile file);
}
