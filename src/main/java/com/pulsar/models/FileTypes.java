package com.pulsar.models;

public enum FileTypes {
    RESUME(1),
    DEGREE(2);

    int type;

    FileTypes(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
