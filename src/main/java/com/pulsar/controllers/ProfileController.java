package com.pulsar.controllers;

import com.pulsar.payload.request.LSAProfileRequest;
import com.pulsar.payload.request.ProfileRequest;
import com.pulsar.security.services.UserDetailsImpl;
import com.pulsar.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/parent/create")
    public ResponseEntity<?> createParentProfile(@Valid @RequestBody ProfileRequest profileRequest) throws Exception {
        UserDetailsImpl userDetails = getLoggedInUser();
        return profileService.createParentProfile(userDetails.getId(), profileRequest);
    }

    @PostMapping("/lsa/update")
    public ResponseEntity<?> updateLSAProfile(@Valid @RequestBody LSAProfileRequest request) throws Exception {
        UserDetailsImpl userDetails = getLoggedInUser();
        return profileService.updateLSAProfile(userDetails.getId(), request);
    }

    @PostMapping("/lsa/cv/upload")
    public ResponseEntity<String> uploadCVFile(@RequestParam("file") MultipartFile file) throws Exception {
        UserDetailsImpl userDetails = getLoggedInUser();
        return profileService.uploadLSACV(userDetails.getId(), file);
    }

    @PostMapping("/lsa/education/upload")
    public ResponseEntity<String> uploadEducationFile(@RequestParam("file") MultipartFile file) throws Exception {
        UserDetailsImpl userDetails = getLoggedInUser();
        return profileService.uploadLSACV(userDetails.getId(), file);
    }

    @PostMapping("/lsa/other/upload")
    public ResponseEntity<String> uploadOtherFiles(@RequestParam("file") MultipartFile file) throws Exception {
        UserDetailsImpl userDetails = getLoggedInUser();
        return profileService.uploadLSACV(userDetails.getId(), file);
    }

    private UserDetailsImpl getLoggedInUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new Exception("Invalid request");
        }
        return (UserDetailsImpl) authentication.getPrincipal();
    }

}
