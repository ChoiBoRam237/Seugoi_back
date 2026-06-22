package com.example.seugoi_back.Study.enums;

public enum StudyFilterType {
    ALL("전체"),
    MY_STUDY("만든 스터디"),
    JOINED("가입한 스터디");

    private final String description;

    StudyFilterType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}