package com.example.seugoi_back.Study.enums;

public enum StudySortType {
    LATEST("최신순"),
    POPULAR("인기순"),
    NAME("이름순");

    private final String description;

    StudySortType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
