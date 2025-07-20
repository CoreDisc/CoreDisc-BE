package com.coredisc.application.service.block;

import com.coredisc.common.converter.BlockConverter;
import com.coredisc.domain.block.Block;
import com.coredisc.domain.block.BlockRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.infrastructure.repository.block.queryDsl.QueryBlockRepository;
import com.coredisc.presentation.dto.block.BlockResponseDTO;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockQueryServiceImpl implements BlockQueryService {

    public final QueryBlockRepository queryBlockRepository;

    @Override
    public CursorDTO<BlockResponseDTO.BlockedDTO> getBlockedList(Member member, Long cursorId, Pageable pageable) {
        List<Block> blocks = queryBlockRepository.findBlockedsByBlocker(member, cursorId, pageable);

        boolean hasNext = blocks.size() > pageable.getPageSize();
        if (hasNext) {
            blocks = blocks.subList(0, pageable.getPageSize());
        }

        List<BlockResponseDTO.BlockedDTO> dtos = blocks.stream()
                .map(BlockConverter::toBlockedDTO)
                .toList();

        return new CursorDTO<>(dtos, hasNext);
    }
}
