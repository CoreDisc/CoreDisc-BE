package com.coredisc.application.service.disc;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.exception.handler.DiscHandler;
import com.coredisc.domain.disc.Disc;
import com.coredisc.domain.disc.DiscRepository;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscQueryServiceImpl implements DiscQueryService {

    private final DiscRepository discRepository;

    @Override
    public Page<Disc> getMyDiscList(Member member, Pageable pageable) {
        Page<Disc> discPage = discRepository.findByMember(member, pageable);

        if (discPage.isEmpty()) {
            if (pageable.getPageNumber() > 0) {
                throw new DiscHandler(ErrorStatus.PAGE_OUT_OF_BOUNDS);
            } else {
                throw new DiscHandler(ErrorStatus.DISC_NOT_FOUND);
            }
        }

        return discPage;
    }

    @Override
    public Disc getDisc(Long discId, Member member) {
        return discRepository.findByIdAndMember(discId, member).orElseThrow(() -> new DiscHandler(ErrorStatus.DISC_NOT_FOUND));
    }
}