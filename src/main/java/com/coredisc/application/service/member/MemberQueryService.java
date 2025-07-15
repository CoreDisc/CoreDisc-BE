package com.coredisc.application.service.member;


import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.member.MemberResponseDTO;
import org.springframework.data.domain.Pageable;

public interface MemberQueryService {

    // CurrentMemberResolver를 위해 Member 찾기
    Member getMemberByUsername(String username);

    // 마이홈(본인) 사용자 정보 조회
    MemberResponseDTO.MyHomeInfoDTO getMyHomeInfo(Member member);

    // 마이홈(타사용자) 사용자 정보 조회
    MemberResponseDTO.UserHomeInfoDTO getUserHomeInfo(Member member, String targetUsername);

    // 마이홈(본인) 게시글 리스트 조회
    CursorDTO<MemberResponseDTO.MyHomePostDTO> getMyHomePosts(Member member, Long cursorId, Pageable page);

    // 마이홈(타사용자) 게시글 리스트 조회
    CursorDTO<MemberResponseDTO.UserHomePostDTO> getUserHomePosts(Member member, String targetUsername, Long cursorId, Pageable page);

}
