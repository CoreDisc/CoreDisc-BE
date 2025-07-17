package com.coredisc.application.service.block;

import com.coredisc.domain.block.Block;
import com.coredisc.domain.block.BlockRepository;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockQueryServiceImpl implements BlockQueryService {

    private final BlockRepository blockRepository;

    @Override
    public List<Block> getBlockeds(Member member) {

        return blockRepository.findAllByBlocked(member);
    }
}
