package com.example.seugoi_back.Study.enums;

import lombok.Getter;

@Getter
public enum StudyStatus {
    STUDYING("진행중"),
    FINISHED("종료");

    private final String description;

    StudyStatus(String description) {
        this.description = description;
    }

}
