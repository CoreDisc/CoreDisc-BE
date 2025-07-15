package com.coredisc.application.service.disc;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.exception.handler.DiscHandler;
import com.coredisc.domain.disc.Disc;
import com.coredisc.domain.disc.DiscRepository;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscQueryServiceImpl implements DiscQueryService {

    private final DiscRepository discRepository;

    @Override
    public List<Disc> getMyDiscList(Member member) {
        List<Disc> discList = discRepository.findByMember(member);
        if (discList.isEmpty()) {
            throw new DiscHandler(ErrorStatus.DISC_NOT_FOUND);
        }
        return discList;
    }

    @Override
    public Disc getDisc(Long discId, Member member) {
        return discRepository.findByIdAndMember(discId, member).orElseThrow(() -> new DiscHandler(ErrorStatus.DISC_NOT_FOUND));
    }
}