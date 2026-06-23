package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Study.dto.request.CommonStudyRequestDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.StudyBookmark;
import com.example.seugoi_back.Study.repository.StudyBookmarkRepository;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyBookmarkService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final StudyBookmarkRepository studyBookmarkRepository;

    @Transactional // 스터디 북마크 Service
    public Map<String, Object> bookmarkStudy(Long userCode, Long studyCode) {
        User user = userRepository.findById(userCode).orElseThrow();
        Study study = studyRepository.findById(studyCode).orElseThrow();

        Optional<StudyBookmark> bookmark =
            studyBookmarkRepository.findByUser_CodeAndStudy_Code(userCode, studyCode);

        // 이미 북마크가 되어 있을 경우 북마크 해제
        if (bookmark.isPresent()) {
            studyBookmarkRepository.delete(bookmark.get());
            return Map.of(
                "userCode", user.getCode(),
                "studyCode", study.getCode(),
                "bookmarked", false
            );
        }

        StudyBookmark studyBookmark = StudyBookmark.builder()
            .user(user)
            .study(study)
            .build();

        studyBookmarkRepository.save(studyBookmark);
        return Map.of(
            "code", studyBookmark.getCode(),
            "userCode", user.getCode(),
            "studyCode", study.getCode(),
            "bookmarked", true
        );
    }
}
