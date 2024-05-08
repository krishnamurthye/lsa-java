package com.pulsar.payload.response;


import java.util.Map;

public class LSAProfileResponse {

    private Map<String, String> data;
    private LSAExperienceResponse lsaExperience;
    private Map<String, String> files;

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public LSAExperienceResponse getLsaExperience() {
        return lsaExperience;
    }

    public void setLsaExperience(LSAExperienceResponse lsaExperience) {
        this.lsaExperience = lsaExperience;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }
}
