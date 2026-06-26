package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Study.dto.response.StudySearchKeywordResponseDto;
import com.example.seugoi_back.Study.entity.StudySearchKeyword;
import com.example.seugoi_back.Study.repository.StudySearchKeywordRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudySearchKeywordService {
    private final UserRepository userRepository;
    private final StudySearchKeywordRepository studySearchKeywordRepository;

    @Transactional // 검색어 저장 Service
    public void saveSearchKeyword(Long userCode, String keyword) {
        User user = userRepository.findById(userCode)
            .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        StudySearchKeyword searchKeyword =
                studySearchKeywordRepository.findByUser_CodeAndKeyword(userCode, keyword)
                        .orElse(null);

        if (searchKeyword == null) {
            StudySearchKeyword responseDto = StudySearchKeyword.builder()
                .user(user)
                .keyword(keyword)
                .searchedAt(LocalDateTime.now())
                .build();

            studySearchKeywordRepository.save(responseDto);
        } else {
            searchKeyword.updateSearchedAt();
        }
    }

    @Transactional // 검색어 조회 Service
    public List<StudySearchKeywordResponseDto> findKeywordByCode(Long userCode) {
        List<StudySearchKeyword> searchKeyword =
                studySearchKeywordRepository.findByUser_CodeOrderBySearchedAtDesc(userCode);

        return searchKeyword.stream()
            .map(search -> StudySearchKeywordResponseDto.builder()
                .code(search.getCode())
                .keyword(search.getKeyword())
                .build())
            .toList();
    }

    @Transactional // 검색어 전체 삭제 Service
    public void deleteAllKeyword(Long userCode) {
        studySearchKeywordRepository.deleteByUser_Code(userCode);
    }

    @Transactional // 검색어 삭제 Service
    public void deleteKeyword(Long keywordCode) {
        studySearchKeywordRepository.deleteById(keywordCode);
    }
}
