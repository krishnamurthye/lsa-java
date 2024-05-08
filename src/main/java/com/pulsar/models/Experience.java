package com.pulsar.models;


import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Objects;

@Document(collection = "experience")
public class Experience {

    private String school;
    private String stdGrade;
    private LocalDate startDate;
    private LocalDate endDate;
    private String rating;

    @DBRef
    private LSAPProfile profile;


    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getStdGrade() {
        return stdGrade;
    }

    public void setStdGrade(String stdGrade) {
        this.stdGrade = stdGrade;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public LSAPProfile getProfile() {
        return profile;
    }

    public void setProfile(LSAPProfile profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Experience that = (Experience) o;
        return Objects.equals(school, that.school) && Objects.equals(stdGrade, that.stdGrade) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(school, stdGrade, startDate, endDate, rating);
    }
}
