package com.pulsar.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "LSAPProfile")
public class LSAPProfile {

    @Id
    private String id;

    @DBRef
    private Set<FileDocument> fileDocuments;

    private LocalDate dateOfBirth;

    private String NationalityCode;

    private String ethnicityCode;

    private String educationCode;

    private String specializationCode;

    @DBRef
    private Set<Experience> experience;

    @DBRef
    private User user;

    private String name;

    private int gender;

    @DBRef
    private Set<FilesInfo> files;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<FileDocument> getFileDocuments() {
        return fileDocuments;
    }

    public void setFileDocuments(Set<FileDocument> fileDocuments) {
        this.fileDocuments = fileDocuments;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationalityCode() {
        return NationalityCode;
    }

    public void setNationalityCode(String nationalityCode) {
        NationalityCode = nationalityCode;
    }

    public String getEthnicityCode() {
        return ethnicityCode;
    }

    public void setEthnicityCode(String ethnicityCode) {
        this.ethnicityCode = ethnicityCode;
    }

    public String getEducationCode() {
        return educationCode;
    }

    public void setEducationCode(String educationCode) {
        this.educationCode = educationCode;
    }

    public String getSpecializationCode() {
        return specializationCode;
    }

    public void setSpecializationCode(String specializationCode) {
        this.specializationCode = specializationCode;
    }

    public Set<Experience> getExperience() {
        return experience;
    }

    public void setExperience(Set<Experience> experience) {
        this.experience = experience;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Set<FilesInfo> getFiles() {
        return files;
    }

    public void setFiles(Set<FilesInfo> files) {
        this.files = files;
    }

    public void addFiles(FilesInfo file) {
        if (this.files == null) {
            this.files = new HashSet<>();
        }
        this.files.add(file);
    }
}
