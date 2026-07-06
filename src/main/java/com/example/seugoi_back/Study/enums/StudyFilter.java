package com.example.seugoi_back.Study.enums;

import lombok.Getter;

@Getter
public enum StudyFilter {
    ALL("전체"),
    MY_STUDY("만든 스터디"),
    JOINED("가입한 스터디");

    private final String description;

    StudyFilter(String description) {
        this.description = description;
    }

}