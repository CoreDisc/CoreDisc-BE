package com.coredisc.application.service.block;

import com.coredisc.domain.block.Block;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.block.BlockResponseDTO;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlockQueryService {

    // 차단한 유저 목록 조회
    CursorDTO<BlockResponseDTO.BlockedDTO> getBlockedList(Member member, Long cursorId, Pageable pageable);
}
