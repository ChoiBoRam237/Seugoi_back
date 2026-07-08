package com.example.seugoi_back.Study.service;

import com.example.seugoi_back.Chat.entity.ChatRoom;
import com.example.seugoi_back.Chat.repository.ChatRoomRepository;
import com.example.seugoi_back.Chat.service.ChatRoomMemberService;
import com.example.seugoi_back.Common.exception.CustomException;
import com.example.seugoi_back.Common.exception.ErrorCode;
import com.example.seugoi_back.Study.dto.request.CommonStudyRequestDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.entity.StudyJoin;
import com.example.seugoi_back.Study.repository.StudyJoinRepository;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyJoinService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private  final StudyJoinRepository studyJoinRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberService chatRoomMemberService;

    @Transactional // 스터디 가입 Service
    public StudyJoin joinStudy(Long userCode, Long studyCode) {
        User user = userRepository.findById(userCode)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Study study = studyRepository.findById(studyCode)
            .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findByStudy_Code(studyCode)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        if (studyJoinRepository
            .findByUser_CodeAndStudy_Code(userCode, studyCode)
            .isPresent()
        ) {
            throw new IllegalArgumentException("이미 가입한 사용자입니다.");
        }

        StudyJoin studyJoin = StudyJoin.builder()
            .user(user)
            .study(study)
            .build();

        // 가입한 인원수 증가
        study.increaseJoinCount();

        // 채팅방 자동 가입
        chatRoomMemberService.joinChatRoom(userCode, chatRoom.getCode());

        return studyJoinRepository.save(studyJoin);
    }

    @Transactional // 스터디 code에 맞는 가입자 데이터 삭제
    public void deleteByStudyCode(Long studyCode) {
        studyJoinRepository.deleteByStudy_Code(studyCode);
    }
}
