package com.pulsar.models;

import java.util.LinkedHashMap;
import java.util.Map;

public enum FileType {

    CV(1),
    EDUCATIONAL_CERTIFICATE(2),
    SPECIAL_CERTIFICATE(3),
    OTHER_CERTIFICATE(4);

    int type;

    FileType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    private static final Map<Integer, FileType> FILE_TYPE_MAP = new LinkedHashMap<>();

    static {
        for (FileType fileType : FileType.values()) {
            FILE_TYPE_MAP.put(fileType.type, fileType);
        }
    }

    public static FileType getFileType(int fileType) {
        return FILE_TYPE_MAP.get(fileType);
    }


}
