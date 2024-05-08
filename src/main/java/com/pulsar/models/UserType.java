package com.pulsar.models;

import java.util.LinkedHashMap;
import java.util.Map;

public enum UserType {

    LSA(1),
    PARENT(2),
    ADMIN(3),
    COUNCILLOR(4);

    int type;

    UserType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    private static final Map<Integer, UserType> USER_TYPE_MAP = new LinkedHashMap<>();

    static {
        for (UserType userType : UserType.values()) {
            USER_TYPE_MAP.put(userType.type, userType);
        }
    }

    public static UserType getUserType(int userType) {
        return USER_TYPE_MAP.get(userType);
    }


}
