package com.pulsar.payload.request;


import java.util.Map;

public class LSAProfileRequest {

    private Map<String, String> data;
    private LSAExperienceRequest lsaExperience;

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public LSAExperienceRequest getLsaExperience() {
        return lsaExperience;
    }

    public void setLsaExperience(LSAExperienceRequest lsaExperience) {
        this.lsaExperience = lsaExperience;
    }
}
