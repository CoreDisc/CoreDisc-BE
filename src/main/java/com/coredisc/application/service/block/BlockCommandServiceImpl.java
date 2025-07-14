package com.coredisc.application.service.block;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.BlockConverter;
import com.coredisc.common.exception.handler.BlockHandler;
import com.coredisc.common.exception.handler.MemberHandler;
import com.coredisc.domain.block.Block;
import com.coredisc.domain.block.BlockRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockCommandServiceImpl implements BlockCommandService {

    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;

    @Override
    public Block block(Member member, Long targetId) {

        if (member.getId().equals(targetId)) {
            throw new BlockHandler(ErrorStatus.SELF_BLOCK_NOT_ALLOWED);
        }

        Member target = memberRepository.findById(targetId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 이미 차단한 이력이 있을 경우
        if (blockRepository.existsByBlockerAndBlocked(member, target)) {
            throw new BlockHandler(ErrorStatus.ALREADY_BLOCKING);
        }

        Block block = BlockConverter.toBlock(member, target);

        return blockRepository.save(block);
    }

    @Override
    public void unblock(Member member, Long targetId) {
        if (member.getId().equals(targetId)) {
            throw new BlockHandler(ErrorStatus.SELF_UNBLOCK_NOT_ALLOWED);
        }

        Member target = memberRepository.findById(targetId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 차단한 이력이 없을 경우
        if (!blockRepository.existsByBlockerAndBlocked(member, target)) {
            throw new BlockHandler(ErrorStatus.BLOCK_NOT_FOUND);
        }

        Block block = blockRepository.findByBlockerAndBlocked(member, target);

        blockRepository.delete(block);
    }
}
