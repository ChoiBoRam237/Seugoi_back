package com.example.seugoi_back.Study.enums;

import lombok.Getter;

@Getter
public enum StudySort {
    LATEST("최신순"),
    POPULAR("인기순"),
    NAME("이름순");

    private final String description;

    StudySort(String description) {
        this.description = description;
    }

}
