package com.pulsar.models;

import java.util.LinkedHashMap;
import java.util.Map;

public enum Gender {
    MALE(1),
    FEMALE(2);

    int type;

    Gender(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    private static final Map<Integer, Gender> GENDER_TYPE_MAP = new LinkedHashMap<>();

    static {
        for (Gender gender : Gender.values()) {
            GENDER_TYPE_MAP.put(gender.type, gender);
        }
    }

    public static Gender getGenderType(int userType) {
        return GENDER_TYPE_MAP.get(userType);
    }

}
