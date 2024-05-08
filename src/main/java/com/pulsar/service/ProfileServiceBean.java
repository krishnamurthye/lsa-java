package com.pulsar.service;

import com.pulsar.models.*;
import com.pulsar.payload.request.LSAExperienceRequest;
import com.pulsar.payload.request.LSAProfileRequest;
import com.pulsar.payload.request.ProfileRequest;
import com.pulsar.payload.response.MessageResponse;
import com.pulsar.repository.AddressRepository;
import com.pulsar.repository.ProfileRepository;
import com.pulsar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Service
public class ProfileServiceBean implements ProfileService, com.pulsar.payload.Constants {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;


    public ResponseEntity<?> createParentProfile(String loggedInUserId, ProfileRequest profileRequest) throws Exception {

        User user = getLSAUser(loggedInUserId);
        Set<Address> address = user.getAddresses();
        if (address.isEmpty()) {
            address.add(new Address());
        }
        Address address1 = address.iterator().next();
        address1.setAddressType(profileRequest.getAddressType());
        address1.setStreetAddress(profileRequest.getStreetAddress());
        address1.setCity(profileRequest.getCity());
        address1.setPostalCode(profileRequest.getPostalCode());

        addressRepository.save(address1);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("success", 200));
    }


    @Override
    public ResponseEntity<?> updateLSAProfile(String loggedInUserId, LSAProfileRequest request) throws Exception {

        if (request == null || request.getData() == null || request.getData().isEmpty()) {
            throw new Exception("Invalid request");
        }
        User loggedInUser = getLSAUser(loggedInUserId);
        LSAPProfile lsapProfile = profileRepository.findByUser(loggedInUser).orElse(new LSAPProfile());
        updateName(request, lsapProfile);
        updateDOB(request, lsapProfile);
        updateGender(request, lsapProfile);
        updateNationality(request, lsapProfile);
        updateEducation(request, lsapProfile);
        updateEthnicity(request, lsapProfile);
        updateSpecialization(request, lsapProfile);
        profileRepository.save(lsapProfile);
        return ResponseEntity.ok(new MessageResponse("success", 200));
    }

    @Override
    public ResponseEntity<String> uploadLSACV(String loggedInUserId, MultipartFile file) {
        return processFile(loggedInUserId, file, FileType.CV);
    }

    @Override
    public ResponseEntity<String> uploadEducationalFile(String loggedInUserId, MultipartFile file) {
        return processFile(loggedInUserId, file, FileType.EDUCATIONAL_CERTIFICATE);
    }

    @Override
    public ResponseEntity<String> uploadOtherFile(String loggedInUserId, MultipartFile file) {
        return processFile(loggedInUserId, file, FileType.OTHER_CERTIFICATE);
    }

    private ResponseEntity<String> processFile(String loggedInUserId, MultipartFile file, FileType fileType) {
        try {

            User loggedInUser = getLSAUser(loggedInUserId);
            LSAPProfile lsapProfile = profileRepository.findByUser(loggedInUser).orElse(new LSAPProfile());
            // Store the file in MongoDB using GridFS
            String fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType()).toString();

            // Save file metadata in a separate collection (FileInfo)
            FilesInfo fileInfo = new FilesInfo();
            fileInfo.setFilename(file.getOriginalFilename());
            fileInfo.setFileType(fileType.getType());
            fileInfo.setLocalDate(Calendar.getInstance().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            mongoTemplate.save(fileInfo);
            lsapProfile.addFiles(fileInfo);
            profileRepository.save(lsapProfile);

            return ResponseEntity.ok("File uploaded successfully. fileType:" + fileType +" FileId: " + fileId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("File upload failed: " + e.getMessage());
        }
    }

    public void updateName(LSAProfileRequest req, LSAPProfile lsapProfile) {
        String code = req.getData().get(USER_NAME);
        if (code == null || code.isEmpty()) {
            return;
        }
        lsapProfile.setName(req.getData().get(USER_NAME));
    }

    public void updateDOB(LSAProfileRequest req, LSAPProfile lsapProfile) throws Exception {
        String code = req.getData().get(DOB);
        if (code == null || code.isEmpty()) {
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(req.getData().get(DOB)));

        if (cal.getTimeInMillis() != Long.parseLong(code)) {
            throw new Exception("Invalid request");
        }
        lsapProfile.setDateOfBirth(cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public void updateGender(LSAProfileRequest req, LSAPProfile lsapProfile) throws Exception {
        String code = req.getData().get(GENDER);
        if (code == null || code.isEmpty()) {
            return;
        }
        Gender gender = Gender.getGenderType(Integer.parseInt(code));
        if (gender == null) {
            throw new Exception("Invalid request");
        }
        lsapProfile.setGender(gender.getType());
    }

    public void updateNationality(LSAProfileRequest req, LSAPProfile lsapProfile) {
        String code = req.getData().get(NATIONALITY_CODE);
        if (code == null || code.isEmpty()) {
            return;
        }
        lsapProfile.setNationalityCode(code);
    }

    public void updateEducation(LSAProfileRequest req, LSAPProfile lsapProfile) {
        String code = req.getData().get(EDUCATION_CODE);
        if (code == null || code.isEmpty()) {
            return;
        }
        lsapProfile.setEducationCode(code);
    }

    public void updateEthnicity(LSAProfileRequest req, LSAPProfile lsapProfile) {
        String code = req.getData().get(ETHNICITY_CODE);
        if (code == null || code.isEmpty()) {
            return;
        }
        lsapProfile.setEthnicityCode(code);
    }

    public void updateSpecialization(LSAProfileRequest req, LSAPProfile lsapProfile) {
        String code = req.getData().get(SPECIALIZATION_CODE);
        if (code == null || code.isEmpty()) {
            return;
        }
        lsapProfile.setSpecializationCode(code);
    }

    public void updateExperience(Experience experience, LSAExperienceRequest req) throws Exception {
        if (req == null) {
            return;
        }

        if (req.getStartDate() < 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(req.getStartDate());
            experience.setStartDate(cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            if (req.getStartDate() != cal.getTimeInMillis()) {
                throw new Exception("Invalid request");
            }
        }

        if (req.getEndDate() < 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(req.getEndDate());
            experience.setEndDate(cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            if (req.getEndDate() != cal.getTimeInMillis()) {
                throw new Exception("Invalid request");
            }
        }

        String grade = req.getStudentGrade();
        if (grade == null || grade.isEmpty()) {
            return;
        }
        experience.setStdGrade(grade);

        String rating = req.getRating();
        if (rating == null || rating.isEmpty()) {
            return;
        }
        experience.setRating(rating);

    }

    private User getLSAUser(String id) throws Exception {
        return userRepository.findByIdUserType(id, UserType.LSA.getType())
                .orElseThrow(() -> new Exception("Invalid request"));
    }

    private User getParentUser(String id) throws Exception {
        return userRepository.findByIdUserType(id, UserType.PARENT.getType())
                .orElseThrow(() -> new Exception("Invalid request"));
    }
}
